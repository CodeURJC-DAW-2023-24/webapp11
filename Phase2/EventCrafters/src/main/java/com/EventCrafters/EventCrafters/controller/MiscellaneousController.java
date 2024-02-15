package com.EventCrafters.EventCrafters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MiscellaneousController {

	@GetMapping("/error")
	public String err(Model model) {
		// To-do: implement the whole thing
		return "error";
	}

	@GetMapping("/other")
	public String other(Model model) {
		// To-do: implement the whole thing
		return "other";
	}
}
