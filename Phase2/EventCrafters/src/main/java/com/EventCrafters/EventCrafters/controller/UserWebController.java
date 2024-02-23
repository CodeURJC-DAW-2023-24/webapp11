package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.CategoryService;
import com.EventCrafters.EventCrafters.service.TokenService;
import com.EventCrafters.EventCrafters.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;
import java.util.Optional;

@Controller
public class UserWebController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	private Map<String, TokenService> tokens = new HashMap<>();

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
		if (request.isUserInRole("USER")) {
			model.addAttribute("show", "none");
		} else {
			List<Category> c = categoryService.findAjax(0,1);
			model.addAttribute("category",c);
			model.addAttribute("show","block");
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		Optional<User> user = userService.findByUserName(currentUsername);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());
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
		user.setRole("USER");
		userService.save(user);
		//log them in somehow
		return "redirect:/login"; // or previous screen
	}

	@PostMapping("/IsUsernameTaken")
	public ResponseEntity<String> isUserNameTaken(@RequestBody String body ) {
		Optional<User> byName = userService.findByUserName(body);
		if (byName.isPresent()){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
		} else {
			return ResponseEntity.ok("Username is available");
		}
	}

	@GetMapping("/changePassword/{nickname}")
	public String changePassword(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "change_password";
	}

	@GetMapping("/recoverPassword/{user}")
	public String recoverPassword(Model model, @PathVariable String user) {
		Optional<User> userOptional = userService.findByUserName(user);
        if (userOptional.isPresent()) {
            TokenService tokenService = new TokenService(userOptional.get());
			tokens.put(userOptional.get().getUsername(), tokenService);
			String link = "https://localhost:8443/recoverPassword/" + userOptional.get().getUsername() +"/randomToken?token=" + tokenService.getToken();
			sendEmail(link);
        }
		return "emailSent";
	}

	@GetMapping("/recoverPassword/{user}/randomToken")
	public String recoverPasswordWithToken(Model model, @PathVariable String user, @RequestParam("token") String token) {
		Optional<User> userOptional = userService.findByUserName(user);
		if (userOptional.isPresent()) {
			TokenService tokenService = tokens.get(userOptional.get().getUsername());
			if (!tokenService.isValid() || !tokenService.getToken().equals(token)){
				return "invalidLink";
			}
			model.addAttribute("recover", true);
			model.addAttribute("token", tokenService.getToken());
			model.addAttribute("username",userOptional.get().getUsername());
		}
		return "change_password";
	}

	@PostMapping("/recoverPassword/{user}/randomToken")
	public String sendRecoverPasswordWithToken(Model model, @PathVariable String user,
											   @RequestParam("token") String token, @RequestParam("password") String password) {
		Optional<User> userOptional = userService.findByUserName(user);
		if (userOptional.isPresent()) {
			TokenService tokenService = tokens.get(userOptional.get().getUsername());
			if (!tokenService.isValid() || !token.equals(tokenService.getToken())){
				model.addAttribute("valid", false);
				return "invalidLink";
			}
			tokens.remove(user);
			userOptional.get().setEncodedPassword(passwordEncoder.encode(password));
			//tell the user the operation was successful
			userService.save(userOptional.get());
		}
		return "redirect:/login";
	}

	@PostMapping("/changePassword/{nickname}")
	public String sendChangePassword(Model model, @PathVariable String nickname) {
		// To-do: implement the whole thing
		return "redirect:/profile"; // if user was signed in, login screen otherwise
	}

	@PostMapping("/delete-account")
	public String deleteAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		Optional<User> userOptional = userService.findByUserName(currentUsername);
		User user = userOptional.get();
		Long currentUserId = user.getId();
		userService.deleteUserById(currentUserId);
		return "redirect:/logout";
	}


	@PostMapping("/updateProfile")
	public String updateProfile(Model model, @ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		Optional<User> userOptional = userService.findByUserName(currentUsername);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			user.setUsername(updatedUser.getUsername());
			userService.save(user);
			SecurityContextHolder.clearContext();
			return "redirect:/login";
		}

		return "error";
	}


	private void sendEmail(String link){
		//this should send an email
		System.out.println(link);
	}


}
