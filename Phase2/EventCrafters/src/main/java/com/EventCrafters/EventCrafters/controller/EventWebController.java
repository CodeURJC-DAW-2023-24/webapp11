package com.EventCrafters.EventCrafters.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.*;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventWebController {

    @Autowired
    private EventService eventService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    private List<Event> allEvents;

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

        if (allEvents.size() <= nextEventIndex){
            model.addAttribute("events", allEvents.subList(0,allEvents.size()));
            nextEventIndex = allEvents.size();
            model.addAttribute("moreEvents", "none");
        }
        else{
            model.addAttribute("events", allEvents.subList(0,nextEventIndex));
            model.addAttribute("moreEvents", "block");
        }
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
            if (allEvents.size() == nextEventIndex){
                model.addAttribute("lastEvents", "");
            }
            return "moreEvents";
        }

        return "empty";
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
                              @RequestParam("map") String map,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                              @RequestParam("category") Long categoryId,
                              @RequestParam(value = "additionalInfo", required = false) String additionalInfo) {
        try {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(photo.getBytes());
            Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            Event event = new Event(name, photoBlob, description, maxCapacity, price, location, map, start, end, additionalInfo);
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
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);
        Optional<Event> eventOptional = eventService.findById(id);

        List <Event> eventsRegistered = new ArrayList<>();

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            boolean isUserCreatorOrAdmin = false;
            boolean isUserRegistered = false;
            boolean hasUserReviewed = false;
            if (isLoggedIn) {
                String currentUsername = authentication.getName();
                Optional<User> currentUser = userService.findByUserName(currentUsername);
                if (currentUser.isPresent()) {
                    Set<Event> aux = currentUser.get().getRegisteredInEvents();
                    eventsRegistered.addAll(aux);

                    if (event.getCreator().equals(currentUser.get())) {
                        isUserCreatorOrAdmin = true;
                    }
                    isUserRegistered = event.getRegisteredUsers().contains(currentUser.get());
                    hasUserReviewed = reviewService.hasUserReviewedEvent(id, currentUser.get().getId());
                }

                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    isUserCreatorOrAdmin = true;
                }
            }

            int numRegisteredUsers = event.getRegisteredUsers().size();

            String priceDisplay = event.getPrice() == 0.0 ? "Gratis" : String.format("%.2f €", event.getPrice());
            String startDateFormatted = event.getFormattedStartDate();
            String endDateFormatted = event.getFormattedEndDate();
            Duration duration = Duration.between(event.getStartDate().toInstant(), event.getEndDate().toInstant());
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationFormatted = String.format("%d horas y %d minutos", hours, minutes);

            LocalDateTime now = LocalDateTime.now();
            boolean eventFinished = now.isAfter(event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            boolean attendeesCountSet = event.getAttendeesCount() >= 0;

            double averageRating = reviewService.calculateAverageRatingForEvent(id);

            model.addAttribute("hasUserReviewed", hasUserReviewed);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("attendeesCountSet", attendeesCountSet);
            model.addAttribute("eventFinished", eventFinished);
            model.addAttribute("event", event);
            model.addAttribute("priceDisplay", priceDisplay);
            model.addAttribute("startDateFormatted", startDateFormatted);
            model.addAttribute("endDateFormatted", endDateFormatted);
            model.addAttribute("duration", durationFormatted);
            model.addAttribute("logged", isLoggedIn);
            model.addAttribute("isUserCreatorOrAdmin", isUserCreatorOrAdmin);
            model.addAttribute("isUserRegistered", isUserRegistered);
            model.addAttribute("numRegisteredUsers", numRegisteredUsers);

            nextEventIndex = eventsRefreshSize;

            if (eventsRegistered.size() <= nextEventIndex){
                model.addAttribute("otherEvents", eventsRegistered);
                nextEventIndex = eventsRegistered.size();
                model.addAttribute("moreEvents", "none");
            }
            else{
                model.addAttribute("otherEvents", eventsRegistered.subList(0,nextEventIndex));
                model.addAttribute("moreEvents", "block");
            }
            return "eventInfo";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/otherEvents")
    public String otherEvents(Model model) {
        List <Event> eventsRegistered = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);
        if (isLoggedIn) {
            String currentUsername = authentication.getName();
            Optional <User> userOptional = userService.findByUserName(currentUsername);
            if (userOptional.isPresent()){
                User currentUser = userOptional.get();
                eventsRegistered.addAll(currentUser.getRegisteredInEvents());
            }
        }

        int remainingEvents = eventsRegistered.size() - nextEventIndex;
        if (remainingEvents > 0) {
            int endIndex = nextEventIndex + Math.min(eventsRefreshSize, remainingEvents);
            model.addAttribute("events", eventsRegistered.subList(nextEventIndex, endIndex));
            nextEventIndex = endIndex;
            if (eventsRegistered.size() == nextEventIndex){
                model.addAttribute("lastEvents", "");
            }
            return "profileEvents";
        }
        return "empty";
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
            String startDateFormatted = event.getFormattedStartDate();
            String endDateFormatted = event.getFormattedEndDate();
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
            model.addAttribute("numRegisteredUsers", numRegisteredUsers-1);
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


    @GetMapping("/search")
    public String filterByTag(Model model, @RequestParam("categoryId") long id){
        nextEventIndex = eventsRefreshSize;
        this.allEvents = eventService.findByCategory(id);
        AbstractMap.SimpleEntry<List<Event>, Integer> additionalEvents = eventService.getAdditionalEvents(allEvents, nextEventIndex, eventsRefreshSize);
        model.addAttribute("additionalEvents", additionalEvents.getKey());
        nextEventIndex = additionalEvents.getValue();
        if (allEvents.size() == nextEventIndex){
            model.addAttribute("lastEvents", "");
        }
        return "moreEvents";
    }

    @GetMapping("/navbarSearch")
    public String navbarSearch(Model model, @RequestParam("input") String input){
        nextEventIndex = eventsRefreshSize;
        this.allEvents = eventService.findBySearchBar(input);
        AbstractMap.SimpleEntry<List<Event>, Integer> additionalEvents = eventService.getAdditionalEvents(allEvents, nextEventIndex, eventsRefreshSize);
        model.addAttribute("additionalEvents", additionalEvents.getKey());
        nextEventIndex = additionalEvents.getValue();
        if (allEvents.size() == nextEventIndex){
            model.addAttribute("lastEvents", "");
        }
        return "moreEvents";
    }

    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/event/edit/{id}")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado para id :: " + id));

        List<Category> categories = categoryService.findAll();
        model.addAttribute("event", event);
        model.addAttribute("categories", categories);
        return "create_event";
    }

    @PostMapping("/event/edit/{id}")
    public String editEvent(@PathVariable Long id,
                            @RequestParam("name") String name,
                            @RequestParam("photo") MultipartFile photo,
                            @RequestParam("description") String description,
                            @RequestParam("capacity") int maxCapacity,
                            @RequestParam("price") double price,
                            @RequestParam("location") String location,
                            @RequestParam("map") String map,
                            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                            @RequestParam("category") Long categoryId,
                            @RequestParam(value = "additionalInfo", required = false) String additionalInfo) throws IOException, SQLException {

        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado para id :: " + id));

        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(photo.getBytes());

        event.setName(name);
        event.setPhoto(photoBlob);
        event.setDescription(description);
        event.setMaxCapacity(maxCapacity);
        event.setPrice(price);
        event.setLocation(location);
        event.setMap(map);
        event.setStartDate(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
        event.setEndDate(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));
        event.setAdditionalInfo(additionalInfo);
        event.setCategory(category);

        eventService.save(event);

        Set<User> registeredUsers = event.getRegisteredUsers();
        String subject = "Actualización del Evento: " + name;
        for (User user : registeredUsers) {
            String content = generateUpdateEmailContent(name);
            mailService.sendEmail(user, subject, content, true);
        }

        return "redirect:/event/" + id;
    }

    private String generateUpdateEmailContent(String eventName) {
        return String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }" +
                        ".container { background-color: #f8f8f8; border-radius: 10px; padding: 20px; text-align: center; }" +
                        "h2 { color: #4CAF50; }" +
                        "p { margin: 10px 0; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<h2>Uno de los eventos en los que estás inscrito ha sido modificado</h2>" +
                        "<p>El evento '%s' ha recibido importantes actualizaciones.</p>" +
                        "<p>Te invitamos a iniciar sesión en tu cuenta para descubrir todas las novedades.</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>", eventName);
    }


    @GetMapping("/moreEventsProfile/{i}")
    public String moreEventsProfile(Model model, @PathVariable int i){
        List<Event> allEvents = eventService.getAllEvents(i);
        int EventRefreshSize = eventService.getEventsRefreshSize();
        int nextEventIndex = eventService.getNextEventIndex(i);
        int remainingEvents = allEvents.size() - nextEventIndex;

        if (remainingEvents > 0) {
            int endIndex = nextEventIndex + Math.min(EventRefreshSize, remainingEvents);
            model.addAttribute( "events", allEvents.subList(nextEventIndex, endIndex));
            eventService.setNextEventIndex(i, endIndex);
            if (allEvents.size() == endIndex){
                model.addAttribute("lastEvents", "");
            }
            return "profileEvents";
        }

        return "empty";
    }

    @PostMapping("/event/setAttendance/{eventId}")
    public String setEventAttendance(@PathVariable Long eventId, @RequestParam("attendeesCount") Integer attendeesCount) {
        if (attendeesCount != null && attendeesCount >= 0) {
            eventService.updateAttendeesCount(eventId, attendeesCount);
        }
        return "redirect:/event/" + eventId;
    }

}
