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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        User user = userService.findById(review.getUserId()).get();
        Event event = eventService.findById(review.getEventId()).get();
        int rating = review.getRating();
        String text = review.getText();
        return new Review(id, user, event, rating, text);

    }

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

    @GetMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> showCategory(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            ReviewDTO reviewDTO = this.transformToDTO(review.get());
            return ResponseEntity.status(200).body(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/review/new")
    public ResponseEntity<Category> newReview(@RequestBody ReviewDTO review) {
        Review newReview = transformFromDTO(review);
        reviewService.save(newReview);
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> substituteReview(@PathVariable Long id, @RequestBody ReviewDTO review) {
        Optional<Review> oldReview = reviewService.findById(id);
        if (oldReview.isPresent()) {
            review.setId(id);
            Review review1 = transformFromDTO(review);
            reviewService.save(review1);
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    /*
        @PatchMapping("/category/{id}")
        public ResponseEntity<Category> modifyChef(@PathVariable Long id, @RequestBody Category category){
            Optional<Category> oldCategory = categoryService.findById(id);
            if (oldCategory.isPresent()){
                return ResponseEntity.notFound().build();
            }
            //categoryService.modifyToMatch(id,chef);
            return ResponseEntity.ok(categoryService.findById(id).get());
        }

     */
    @DeleteMapping("/review/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            reviewService.delete(id);
            return ResponseEntity.ok(review.get());
        }
        return ResponseEntity.notFound().build();

    }

}
