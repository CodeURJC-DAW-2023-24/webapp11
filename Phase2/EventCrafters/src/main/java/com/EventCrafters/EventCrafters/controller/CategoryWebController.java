package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import com.EventCrafters.EventCrafters.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

@Controller
public class CategoryWebController {

	@Autowired
	private CategoryService categoryService;
	@PostMapping("/newCategory")
	public String newCategory(Model model, @RequestParam String category, @RequestParam String color)  {
		categoryService.save(new Category(category, color));
		// To-do: implement the whole thing
		return "redirect:/profile/admin";
	}
}
