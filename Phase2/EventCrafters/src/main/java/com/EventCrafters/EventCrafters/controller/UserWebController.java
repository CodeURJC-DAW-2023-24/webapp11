package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@Controller
public class UserWebController {


	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror() {
		return "loginerror";
	}

	@PostMapping("/logout") //might not be a simple post
	public String logout(Model model) {
		// To-do: implement the whole thing
		return "login";
	}

	@GetMapping("/profile/{nickname}")
	public String newReview(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "profile";
	}

	@GetMapping("/profile/admin")
	public String showAdminProfile(Model model){
		List<Category> c = categoryRepository.findAll(PageRequest.of(0,1)).getContent();
		model.addAttribute("category",c);
		model.addAttribute("show","block");
		return "profile";
	}

	@GetMapping("categories")
	public String loadCategories(Model model, @RequestParam("page") int page) {
		int pageSize = 1; // Define cuántos elementos quieres por página
		List<Category> c = categoryRepository.findAll(PageRequest.of(page, pageSize)).getContent();
		model.addAttribute("category", c);
		return "categories";
	}

	@GetMapping("/register")
	public String register(Model model) {
		// To-do: implement the whole thing
		return "register";
	}

	@PostMapping("/register")
	public String createAccount(Model model) {
		// To-do: implement the whole thing
		return "profile"; // or previous screen
	}

	@GetMapping("/changePassword/{nickname}")
	public String changePassword(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "change_password";
	}

	@PostMapping("/changePassword/{nickname}")
	public String sendChangePassword(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "profile"; // if user was signed in, login screen otherwise
	}

	@PostMapping("/deleteAccount/{nickname}")
	public String deleteAccount(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "login"; // if user was signed in, login screen otherwise
	}
}
