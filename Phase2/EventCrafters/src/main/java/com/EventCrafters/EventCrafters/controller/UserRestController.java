package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private AjaxService ajaxService;

	private Map<String, TokenService> tokens = new HashMap<>();

	@GetMapping("/me")
	public ResponseEntity<User> currentUser(HttpServletRequest request){
		Principal principal = request.getUserPrincipal();

		if(principal != null) {
			return ResponseEntity.ok(userService.findByUserName(principal.getName()).orElseThrow());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//@GetMapping("/test/test")
	//public ResponseEntity<Event> test(){
	//	Optional<Event> optionalUser = eventService.findById(1L);
	//	return ResponseEntity.ok(optionalUser.get());
	//}


	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(HttpServletRequest request, @PathVariable Long id){
		Principal principal = request.getUserPrincipal();
		Optional<User> optionalUser = userService.findById(id);
		User user;
		if (optionalUser.isPresent()) {
			user = (optionalUser.get());
		} else {
			return ResponseEntity.status(404).build();
		}

		if (principal != null && user.getUsername().equals(principal.getName())) {
			return ResponseEntity.ok(user);
		} else {
			User censoredUser = new User();
			censoredUser.setPhoto(user.getPhoto());
			censoredUser.setName(user.getName());
			censoredUser.setUsername(user.getUsername());
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/new")
	public ResponseEntity<User> newUser(@RequestParam User user){
		if (userService.findByUserName(user.getUsername()).isPresent()) {
			return ResponseEntity.status(409).build(); //409 conflict
		}

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.clearRoles();
		user.setRole("USER");

		if (user.getPhoto()==null){
			user.setDefaultPhoto();
		}
		userService.save(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/users/"+user.getUsername());

		return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> newUser(@RequestParam User user, @PathVariable Long id){
		Optional<User> optUser = userService.findById(id);
		if (optUser.isPresent()) {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.clearRoles();
			user.setRole("USER");
			user.setId(optUser.get().getId());

			if (user.getPhoto() == null) {
				user.setDefaultPhoto();
			}
			userService.save(user);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable Long id){
		Optional<User> optUser = userService.findById(id);
		if (optUser.isPresent()) {
			userService.deleteUserById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}




}
