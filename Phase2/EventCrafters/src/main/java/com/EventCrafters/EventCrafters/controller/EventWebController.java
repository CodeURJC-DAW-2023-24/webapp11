package com.EventCrafters.EventCrafters.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.UserService;
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

    private List<Event> allEvents;

    private int nextEventIndex = 3;
    private int eventsRefreshSize = 3;


    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = isAuthenticated(authentication);

        nextEventIndex = eventsRefreshSize;
        this.allEvents = eventService.findAll();

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

    @GetMapping("/event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "eventInfo";
        } else {
            return "redirect:/";
        }
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
                              @RequestParam(value = "additionalInfo", required = false) String additionalInfo,
                              RedirectAttributes redirectAttributes) {
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
}
