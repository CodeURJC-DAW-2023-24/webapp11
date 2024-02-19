package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryWebController {

	@Autowired
	private CategoryRepository categoryRepository;
	@PostMapping("/newCategory")
	public String newCategory(Model model) {
		// To-do: implement the whole thing
		return "profile";
	}
}
