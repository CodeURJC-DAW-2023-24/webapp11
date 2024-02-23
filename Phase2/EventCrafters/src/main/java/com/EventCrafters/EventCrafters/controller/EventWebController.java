package com.EventCrafters.EventCrafters.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.ReviewService;
import com.EventCrafters.EventCrafters.service.UserService;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.EventCrafters.EventCrafters.service.EventService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventWebController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    private List<Event> allEvents;

    private List<Event> categoryFilteredEvents;

    private int nextEventIndex = 3;
    private int eventsRefreshSize = 3;


    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);
        List<Category> c = categoryService.findAll();

        nextEventIndex = eventsRefreshSize;
        this.allEvents = eventService.findAll();

        // for the filters dropdown-menu
        model.addAttribute("categories", c);

        if (allEvents.isEmpty()){
            model.addAttribute("events", new ArrayList<Event>());
            nextEventIndex = allEvents.size();
        }
        else if (allEvents.size() <= nextEventIndex){
            model.addAttribute("events", allEvents.subList(0,allEvents.size()));
            nextEventIndex = allEvents.size();
        }
        else{
            model.addAttribute("events", allEvents.subList(0,nextEventIndex));
        }
        model.addAttribute("loadmoretype", "/newEvents" );
        model.addAttribute("logged", isLoggedIn);
        return "index";
    }
    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @GetMapping("/newEvents")
    public String newEvents(Model model) {
        int remainingEvents = allEvents.size() - nextEventIndex;

        if (remainingEvents > 0) {
            int endIndex = nextEventIndex + Math.min(eventsRefreshSize, remainingEvents);
            model.addAttribute("additionalEvents", allEvents.subList(nextEventIndex, endIndex));
            nextEventIndex = endIndex;
        }

        return "moreEvents";
    }

    @GetMapping("/create_event")
    public String createEvent(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "create_event";
    }

    @PostMapping("/create_event")
    public String createEvent(@RequestParam("name") String name,
                              @RequestParam("photo") MultipartFile photo,
                              @RequestParam("description") String description,
                              @RequestParam("capacity") int maxCapacity,
                              @RequestParam("price") double price,
                              @RequestParam("location") String location,
                              @RequestParam("coordinates") String coordinates,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                              @RequestParam("category") Long categoryId,
                              @RequestParam(value = "additionalInfo", required = false) String additionalInfo) {
        try {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(photo.getBytes());
            String[] latLong = coordinates.split(",");
            Double latitude = Double.parseDouble(latLong[0].trim());
            Double longitude = Double.parseDouble(latLong[1].trim());
            Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            Event event = new Event(name, photoBlob, description, maxCapacity, price, location, latitude, longitude, start, end, additionalInfo);
            event.setCategory(category);
            category.getEventsInCategories().add(event);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Optional<User> userOpt = userService.findByUserName(currentUsername);

            userOpt.ifPresent(user -> {
                event.setCreator(user);
            });

            eventService.save(event);


        } catch (Exception e) {
        }

        return "redirect:/";
    }

    @GetMapping("/event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);
        Optional<Event> eventOptional = eventService.findById(id);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            boolean isUserCreatorOrAdmin = false;
            boolean isUserRegistered = false;

            if (isLoggedIn) {
                String currentUsername = authentication.getName();
                Optional<User> currentUser = userService.findByUserName(currentUsername);
                if (currentUser.isPresent()) {
                    if (event.getCreator().equals(currentUser.get())) {
                        isUserCreatorOrAdmin = true;
                    }
                    isUserRegistered = event.getRegisteredUsers().contains(currentUser.get());
                }

                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    isUserCreatorOrAdmin = true;
                }
            }


            int numRegisteredUsers = event.getRegisteredUsers().size();

            String priceDisplay = event.getPrice() == 0.0 ? "Gratis" : String.format("%.2f €", event.getPrice());
            String startDateFormatted = formatDate(event.getStartDate());
            String endDateFormatted = formatDate(event.getEndDate());
            Duration duration = Duration.between(event.getStartDate().toInstant(), event.getEndDate().toInstant());
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationFormatted = String.format("%d horas y %d minutos", hours, minutes);

            model.addAttribute("event", event);
            model.addAttribute("priceDisplay", priceDisplay);
            model.addAttribute("startDateFormatted", startDateFormatted);
            model.addAttribute("endDateFormatted", endDateFormatted);
            model.addAttribute("duration", durationFormatted);
            model.addAttribute("logged", isLoggedIn);
            model.addAttribute("isUserCreatorOrAdmin", isUserCreatorOrAdmin);
            model.addAttribute("isUserRegistered", isUserRegistered);
            model.addAttribute("numRegisteredUsers", numRegisteredUsers);

            return "eventInfo";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/ticket/{id}")
    public String showTicket(Model model, @PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);
        Optional<Event> eventOptional = eventService.findById(id);

        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByUserName(currentUsername);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            boolean isUserCreatorOrAdmin = false;
            boolean isUserRegistered = false;

            if (isLoggedIn) {
                if (currentUser.isPresent()) {
                    if (event.getCreator().equals(currentUser.get())) {
                        isUserCreatorOrAdmin = true;
                    }
                    isUserRegistered = event.getRegisteredUsers().contains(currentUser.get());
                }

                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    isUserCreatorOrAdmin = true;
                }
            }

            int numRegisteredUsers = event.getRegisteredUsers().size()+1;

            String priceDisplay = event.getPrice() == 0.0 ? "Gratis" : String.format("%.2f €", event.getPrice());
            String startDateFormatted = formatDate(event.getStartDate());
            String endDateFormatted = formatDate(event.getEndDate());
            Duration duration = Duration.between(event.getStartDate().toInstant(), event.getEndDate().toInstant());
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationFormatted = String.format("%d horas y %d minutos", hours, minutes);

            List<Review> reviews =reviewService.findAll();
            float averageRating = 0.0f;
            for (Review r : reviews){
                averageRating += r.getRating();
            }
            averageRating = averageRating / reviews.size();

            model.addAttribute("event", event);
            model.addAttribute("priceDisplay", priceDisplay);
            model.addAttribute("startDateFormatted", startDateFormatted);
            model.addAttribute("endDateFormatted", endDateFormatted);
            model.addAttribute("duration", durationFormatted);
            model.addAttribute("logged", isLoggedIn);
            model.addAttribute("isUserCreatorOrAdmin", isUserCreatorOrAdmin);
            model.addAttribute("isUserRegistered", isUserRegistered);
            model.addAttribute("numRegisteredUsers", numRegisteredUsers);
            model.addAttribute("reviewsQuantity", reviews.size());
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("userName", currentUser.get().getName());
            model.addAttribute("userNick", currentUser.get().getUsername());
            model.addAttribute("userEmail", currentUser.get().getEmail());

            return "ticket";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/event/register/{eventId}")
    public String registerToEvent(@PathVariable("eventId") Long eventId, Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUserName(username);
            Optional<Event> eventOpt = eventService.findById(eventId);

            if (userOpt.isPresent() && eventOpt.isPresent()) {
                User user = userOpt.get();
                Event event = eventOpt.get();

                if (event.getRegisteredUsers().contains(user)) {
                    return "redirect:/event/" + eventId;
                }

                if (event.getCreator().equals(user)) {
                    return "redirect:/event/" + eventId;
                }

                if (event.getRegisteredUsers().size() >= event.getMaxCapacity()) {
                    return "redirect:/event/" + eventId;
                }

                event.getRegisteredUsers().add(user);
                eventService.save(event);

            }
        } else {
            return "redirect:/login";
        }
        return "redirect:/event/" + eventId;
    }

    @PostMapping("/event/leave/{eventId}")
    public String desapuntarteDelEvento(@PathVariable("eventId") Long eventId, Authentication authentication, Model model) {
        // Check if the user is authenticated
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Optional<User> currentUser = userService.findByUserName(username);
            Optional<Event> eventOpt = eventService.findById(eventId);

            if (currentUser.isPresent() && eventOpt.isPresent()) {
                Event event = eventOpt.get();
                User user = currentUser.get();

                // Check if the currently authenticated user is registered for the event
                if (event.getRegisteredUsers().contains(user)) {
                    // Unregister the user from the event
                    event.getRegisteredUsers().remove(user);
                    // Save the changes to the event
                    eventService.save(event);
                }
            }
        }

        return "redirect:/event/" + eventId;
    }


    @GetMapping("/event/image/{id}")
    @ResponseBody
    public byte[] showEventImage(@PathVariable long id) throws SQLException, IOException {
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()) {
            Blob photoBlob = eventOptional.get().getPhoto();
            int blobLength = (int) photoBlob.length();
            byte[] blobAsBytes = photoBlob.getBytes(1, blobLength);
            photoBlob.free();
            return blobAsBytes;
        } else {
            return new byte[0];
        }
    }

    public static String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(date);
    }

    @GetMapping("/search")
    public String filterByTag(Model model, @RequestParam("categoryId") long id){
        nextEventIndex = eventsRefreshSize;
        this.categoryFilteredEvents = eventService.findByCategory(id);

        if (categoryFilteredEvents.size() <= nextEventIndex){
            model.addAttribute("additionalEvents", categoryFilteredEvents.subList(0,categoryFilteredEvents.size()));
            nextEventIndex = categoryFilteredEvents.size();
        }
        else{
            model.addAttribute("additionalEvents", categoryFilteredEvents.subList(0,nextEventIndex));
        }
        //model.addAttribute("additionalEvents", cL);

        return "moreEvents";
    }

    @GetMapping("/newFilteredEvents")
    public String newFilteredEvents(Model model) {
        int remainingEvents = categoryFilteredEvents.size() - nextEventIndex;

        if (remainingEvents > 0) {
            int endIndex = nextEventIndex + Math.min(eventsRefreshSize, remainingEvents);
            model.addAttribute("additionalEvents", categoryFilteredEvents.subList(nextEventIndex, endIndex));
            nextEventIndex = endIndex;
        }

        return "moreEvents";
    }



}
