package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import com.EventCrafters.EventCrafters.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryWebController {

	@Autowired
	private CategoryService categoryService;
	@PostMapping("/newCategory")
	public String newCategory(Model model, @RequestParam String name, @RequestParam String color)  {
		categoryService.save(new Category(name, color));
		// To-do: implement the whole thing
		return "redirect:/profile";
	}

	@PostMapping("/editCategory/{id}")
	public String editCategory(@PathVariable Long id, @RequestParam String name, @RequestParam String color){
		Optional<Category> c = categoryService.findById(id);
		Category updatedC;
		if (c.isPresent()){
			updatedC = c.get();
		}else {
			return "error";
		}
		updatedC.setColor(color);
		updatedC.setName(name);
		categoryService.save(updatedC);

		return "redirect:/profile";
	}

	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable Long id){
		categoryService.delete(id);
		return "redirect:/profile";
	}

	@GetMapping("categories")
	public String loadCategories(Model model, @RequestParam("page") int page) {
		int pageSize = 1; // Define cuántos elementos quieres por página
		List<Category> c =categoryService.findAjax(page,pageSize);
		if (c.isEmpty()){
			return "empty";
		} else {
			model.addAttribute("category", c);
		}
		return "categories";
	}
}
