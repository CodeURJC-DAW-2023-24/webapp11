package com.EventCrafters.EventCrafters.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ReviewController {
/*
    @Autowired
    private ReviewService service;
*/
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
        return "review";
    }

}
