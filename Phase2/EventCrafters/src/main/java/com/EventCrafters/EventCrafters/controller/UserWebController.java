package com.EventCrafters.EventCrafters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserWebController {
	
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
