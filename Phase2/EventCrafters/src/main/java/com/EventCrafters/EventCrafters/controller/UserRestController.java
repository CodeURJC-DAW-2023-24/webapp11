package com.EventCrafters.EventCrafters.controller;

import com.EventCrafters.EventCrafters.DTO.CensoredUserDTO;
import com.EventCrafters.EventCrafters.DTO.FullUserDTO;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Blob;
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
	public ResponseEntity<FullUserDTO> currentUser(HttpServletRequest request){
		Principal principal = request.getUserPrincipal();
		if(principal != null) {
			return ResponseEntity.ok(new FullUserDTO(userService.findByUserName(principal.getName()).orElseThrow()));
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
	public ResponseEntity<CensoredUserDTO> getUser(HttpServletRequest request, @PathVariable Long id){
		Principal principal = request.getUserPrincipal();
		Optional<User> optionalUser = userService.findById(id);
		User user;
		if (optionalUser.isPresent()) {
			user = (optionalUser.get());
		} else {
			return ResponseEntity.status(404).build();
		}
		if (principal != null && user.getUsername().equals(principal.getName())) {

			return ResponseEntity.ok(new FullUserDTO(user));
		} else {
			return ResponseEntity.ok(new CensoredUserDTO(user));
		}
	}

	@PostMapping("/new")
	public ResponseEntity<FullUserDTO> newUser(@RequestBody User user){
		if (userService.findByUserName(user.getUsername()).isPresent()) {
			return ResponseEntity.status(409).build(); //409 conflict
		}

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.clearRoles();
		user.setRole("USER");
		user.setDefaultPhoto();

		userService.save(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/users/"+user.getUsername());

		FullUserDTO fullUserDTO = new FullUserDTO(user);
		return new ResponseEntity<>(fullUserDTO, headers, HttpStatus.CREATED);
	}

	@GetMapping ("/img/{id}")
	public ResponseEntity<byte[]> showEventImage(@PathVariable long id){
		Optional<User> userOptional = userService.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			try {
				Blob photoBlob = user.getPhoto();
				byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());

				return ResponseEntity
						.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(photoBytes);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

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
