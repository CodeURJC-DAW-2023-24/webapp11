package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.EventDTO;
import com.EventCrafters.EventCrafters.DTO.EventFinishedDTO;
import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.EventService;
import com.EventCrafters.EventCrafters.service.ReviewService;
import com.EventCrafters.EventCrafters.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Blob;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> showEvent(@PathVariable long id){
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()){
            Event event = eventOptional.get();
            EventDTO eventDTO = transformDTO(event);
            return ResponseEntity.ok(eventDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestPart("event") Event event,
                                                @RequestPart("photo") MultipartFile photo) {
        // Check for empty fields in the event
        if (eventHasEmptyFields(event) || photo == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if category exists
        Optional<Category> categoryOpt = categoryService.findById(event.getCategory().getId());
        if (!categoryOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String currentUsername = authentication.getName();
            Optional<User> userOpt = userService.findByUserName(currentUsername);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            event.setCreator(userOpt.get());

            // Set the category
            event.setCategory(categoryOpt.get());

            event.setPhoto(new javax.sql.rowset.serial.SerialBlob(photo.getBytes()));
            // Save the event
            Event savedEvent = eventService.save(event);

            // Transform the saved event to EventDTO
            EventDTO eventDTO = transformDTO(savedEvent);

            // Build the URL created event
            URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
                    .path("/api/events/{id}")
                    .buildAndExpand(savedEvent.getId())
                    .toUri();

            return ResponseEntity.created(location).body(eventDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> showEventImage(@PathVariable long id) {
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            try {
                Blob photoBlob = event.getPhoto();
                byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());

                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photoBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{eventId}/attendees")
    public ResponseEntity<EventDTO> updateEventAttendees(@PathVariable Long eventId, @RequestBody Integer attendeesCount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // User is not authenticated
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String currentUsername = authentication.getName();

        Optional<Event> eventOpt = eventService.findById(eventId);
        if (!eventOpt.isPresent()) {
            // Event not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event event = eventOpt.get();

        if (!isUserAdminOrCreator(currentUsername, event)) {
            // Unauthorized to update the event
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (event.getEndDate().before(new Date()) && attendeesCount != null && attendeesCount >= 0 && attendeesCount <= event.getNumRegisteredUsers()) {
            // Update the attendees count (the event has already ended)
            event.setAttendeesCount(attendeesCount);
            eventService.save(event);

            EventDTO eventDTO = transformDTO(event);
            // Return the "Location" header pointing to the event's URL
            String graphUrl = "https://localhost:8443/api/events/" + eventId;
            return ResponseEntity.ok().header("Location", graphUrl).body(eventDTO);
        } else {
            // Event has not ended yet
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{eventId}/graph")
    public ResponseEntity<Map<String, Integer>> getEventGraphData(@PathVariable Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // User is not authenticated
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String currentUsername = authentication.getName();

        Optional<Event> eventOptional = eventService.findById(eventId);
        if(!eventOptional.isPresent()) {
            return ResponseEntity.notFound().build();

        }
        Event event = eventOptional.get();
        if (!isUserAdminOrCreator(currentUsername, event)) {
            // Unauthorized to update the event
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Map<String, Integer> graphData = new HashMap<>();
        if (event.getAttendeesCount() == -1) {
            // If the attendees count has not been set returning -1 for both registeredUsers and didNotAttend to indicate data is not available
            graphData.put("registeredUsers", -1);
            graphData.put("attendeesCount", -1);
            graphData.put("didNotAttend", -1);
        } else {
            // If attendees count is available, populate the map with actual data
            graphData.put("registeredUsers", event.getNumRegisteredUsers());
            graphData.put("attendeesCount", event.getAttendeesCount());
            int didNotAttend = event.getNumRegisteredUsers() - event.getAttendeesCount();
            graphData.put("didNotAttend", didNotAttend);
        }

        return ResponseEntity.ok(graphData);
    }


    private EventDTO transformDTO(Event event) {
        if (event.getEndDate().before(new Date())) {
            return new EventFinishedDTO(event.getId(), event.getName(), event.getDescription(), event.getMaxCapacity(), event.getPrice(), event.getLocation(), event.getMap(), event.getStartDate(), event.getEndDate(), event.getAdditionalInfo(), event.getCreator().getId(), event.getNumRegisteredUsers(), event.getCategory().getId(), event.getAttendeesCount(), reviewService.calculateAverageRatingForEvent(event.getId()), reviewService.countReviewsForEvent(event.getId()));
        }
        return new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getMaxCapacity(), event.getPrice(), event.getLocation(), event.getMap(), event.getStartDate(), event.getEndDate(), event.getAdditionalInfo(), event.getCreator().getId(), event.getNumRegisteredUsers(), event.getCategory().getId());
    }

    private boolean eventHasEmptyFields(Event event) {
        if (
                event.getName() == null || event.getName().trim().isEmpty() ||
                event.getDescription() == null || event.getDescription().trim().isEmpty() ||
                event.getLocation() == null || event.getLocation().trim().isEmpty() ||
                event.getMap() == null || event.getMap().trim().isEmpty() ||
                event.getCategory() == null || event.getCategory().getId() == null ||
                event.getAdditionalInfo() == null || event.getAdditionalInfo().trim().isEmpty())
        {
            return true;
        }

        if (event.getMaxCapacity() <= 0 || event.getPrice() < 0) {
            return true;
        }

        Date now = new Date();

        if (event.getStartDate() == null || event.getStartDate().before(now)) {
            return true;
        }

        if (event.getEndDate() == null || event.getEndDate().before(event.getStartDate())) {
            return true;
        }

        String mapIframeRegex = "<iframe.*src=\"https?.*\".*></iframe>";
        Pattern pattern = Pattern.compile(mapIframeRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(event.getMap());

        if (!matcher.find()) {
            return true;
        }

        return false;
    }
    private boolean isUserAdminOrCreator(String username, Event event) {
        Optional<User> userOpt = userService.findByUserName(username);
        if (!userOpt.isPresent()) {
            return false;
        }
        User user = userOpt.get();

        // Check if the user has ROLE_ADMIN authority
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Check if the user is the event creator
        boolean isCreator = event.getCreator().equals(user);

        return isAdmin || isCreator;
    }

}

