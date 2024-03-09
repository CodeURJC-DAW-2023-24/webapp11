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

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            /*Category category = categoryService.findById(event.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            byte[] decodedBytes = Base64.getDecoder().decode(event.getPhoto());
            Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(decodedBytes);

            Date start = Date.from(Instant.parse(event.getStartDate().clone().toString()));
            Date end = Date.from(Instant.parse(event.getEndDate().clone().toString()));*/

            Event newEvent = new Event(event.getName(), null, event.getDescription(),
                    event.getMaxCapacity(), event.getPrice(), event.getLocation(),
                    event.getMap(), null, null, event.getAdditionalInfo());

            newEvent.setCategory(null);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Optional<User> userOpt = userService.findByUserName(currentUsername);

            userOpt.ifPresent(newEvent::setCreator);

            eventService.save(newEvent);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el evento");
        }
    }
}
