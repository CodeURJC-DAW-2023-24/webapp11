package com.EventCrafters.EventCrafters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryWebController {

	@PostMapping("/newCategory")
	public String newCategory(Model model) {
		// To-do: implement the whole thing
		return "profile";
	}
}
