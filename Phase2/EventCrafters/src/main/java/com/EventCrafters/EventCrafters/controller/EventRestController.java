package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.EventDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Blob;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/events/{id}")
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

    @PostMapping("/events")
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
                    .path("/event/{id}")
                    .buildAndExpand(savedEvent.getId())
                    .toUri();

            return ResponseEntity.created(location).body(eventDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private EventDTO transformDTO(Event event) {
        Set<Long> registredUsersId = new HashSet<>();
        for (User u : event.getRegisteredUsers()) {
            registredUsersId.add(u.getId());
        }
        Set<Long> reviewId = new HashSet<>();
        for (Review review : event.getReviews()) {
            reviewId.add(review.getId());
        }
        return new EventDTO(event.getId(), event.getName(), event.getAttendeesCount(), event.getDescription(), event.getMaxCapacity(), event.getPrice(), event.getLocation(), event.getMap(), event.getStartDate(), event.getEndDate(), event.getAdditionalInfo(), event.getCreator().getId(), registredUsersId, event.getNumRegisteredUsers(), reviewId, event.getCategory().getId());
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

        return false;
    }
}

