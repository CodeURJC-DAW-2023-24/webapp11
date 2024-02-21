package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserWebController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;

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

	@GetMapping("/profile")
	public String newReview(Model model, HttpServletRequest request) {
		// To-do: implement the whole thing
		if (request.isUserInRole("USER")) {
			model.addAttribute("show", "none");
		} else {
			List<Category> c = categoryService.findAjax(0,1);
			model.addAttribute("category",c);
			model.addAttribute("show","block");
		}
		return "profile";
	}


	@GetMapping("categories")
	public String loadCategories(Model model, @RequestParam("page") int page) {
		int pageSize = 1; // Define cuántos elementos quieres por página
		List<Category> c =categoryService.findAjax(page,pageSize);
		model.addAttribute("category", c);
		return "categories";
	}

	@GetMapping("/register")
	public String register(Model model) {
		// To-do: implement the whole thing
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String createAccount(Model model, User user) {
		// To-do: implement the whole thing
		//if (!userService.findAll().contains(user))

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userService.save(user);
		//log them in somehow
		return "redirect:/login"; // or previous screen
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
