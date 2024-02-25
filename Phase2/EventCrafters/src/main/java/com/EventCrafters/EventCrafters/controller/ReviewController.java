package com.EventCrafters.EventCrafters.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.EventService;
import com.EventCrafters.EventCrafters.service.ReviewService;
import com.EventCrafters.EventCrafters.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ReviewController {

    @Autowired
    private ReviewService service;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/newReview")
    public String newReview(Model model) {
        // To-do: load relevant info into the model
        return "review";
    }
    @PostMapping("/newReview")
    public String reviewCreated(Review r) {
        System.out.println("hola");
        System.out.println(r.getText());
        System.out.println(r.getRating());
        return "redirect:/";
    }

    @GetMapping("/review/event/{id}")
    public String showReviewForm(@PathVariable long id, Model model, Authentication authentication) {
        Optional<Event> eventOptional = eventService.findById(id);
        boolean isLoggedIn = isAuthenticated(authentication);
        if (eventOptional.isPresent() && isLoggedIn) {
            Event event = eventOptional.get();
            LocalDateTime now = LocalDateTime.now();
            boolean eventFinished = now.isAfter(event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            String currentUsername = authentication.getName();
            Optional<User> currentUser = userService.findByUserName(currentUsername);
            boolean isUserRegistered = currentUser.isPresent() && event.getRegisteredUsers().contains(currentUser.get());
            if (eventFinished && isUserRegistered) {
                model.addAttribute("event", event);
                return "review";
            }
        }
        return "redirect:/";
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
