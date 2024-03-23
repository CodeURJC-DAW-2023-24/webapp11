package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.CategoryDTO;
import com.EventCrafters.EventCrafters.DTO.ReviewDTO;
import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.EventService;
import com.EventCrafters.EventCrafters.service.ReviewService;
import com.EventCrafters.EventCrafters.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ReviewRestController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    private ReviewDTO transformToDTO(Review review) {
        Long id = review.getId();
        Long userId = review.getUser().getId();
        Long eventId = review.getEvent().getId();
        int rating = review.getRating();
        String text = review.getText();
        return new ReviewDTO(id, userId, eventId, rating, text);

    }

    private Review transformFromDTO(ReviewDTO review) {
        Long id = review.getId();
        User user;
        if (userService.findById(review.getUserId()).isPresent())
            user = userService.findById(review.getUserId()).get();
        else
            user = null;
        Event event;
        if (eventService.findById(review.getEventId()).isPresent())
            event = eventService.findById(review.getEventId()).get();
        else
            event = null;
        int rating = review.getRating();
        String text = review.getText();
        return new Review(id, user, event, rating, text);

    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    /*
    @GetMapping("/reviews")
    public List<ReviewDTO> showReviews() {
        List<Review> all = reviewService.findAll();
        List<ReviewDTO> answer = new ArrayList<>();
        for (Review r : all) {
            ReviewDTO reviewDTO = transformToDTO(r);
            answer.add(reviewDTO);
        }

        return answer;
    }
     */

    /*
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> showCategory(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            ReviewDTO reviewDTO = this.transformToDTO(review.get());
            return ResponseEntity.status(200).body(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

     */
    // no se devuelve la url de la review creada, porque se supone que la web no tiene la opción de ver las reviews
    @PostMapping("/reviews")
    @Operation(summary = "Create a review for an event that has ended and for which the user had signed up.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation not permitted", content = @Content)
    })
    public ResponseEntity<String> newReview(@RequestBody ReviewDTO review) {
        Review newReview = transformFromDTO(review);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(isAuthenticated(authentication)){
            Optional<User> user = userService.findByUserName(authentication.getName());
            Optional<Event> event = eventService.findById(review.getEventId());
            if (user.isPresent() && event.isPresent()){
                if (user.get().getId().equals(review.getUserId()) &&
                        event.get().getRegisteredUsers().contains(user.get()) &&
                        event.get().getEndDate().before(new Date())){
                    reviewService.save(newReview);
                    return ResponseEntity.status(201).body("");
                }
            }
        }else if (!isAuthenticated(authentication)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.badRequest().build();
    }



}
