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
		Optional<User> optionalUser = userService.findById(id);
		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(404).build();
		}

		User user = (optionalUser.get());
		Principal principal = request.getUserPrincipal();
		if (principal != null){
			Optional<User> userPrincipalOptional = userService.findByUserName(principal.getName());
			if (userPrincipalOptional.isPresent()) {
				User userPrincipal = userPrincipalOptional.get();
				if (userPrincipal.hasRole("ADMIN")){
					return ResponseEntity.ok(new FullUserDTO(user));
				}
			}
		}

		if (principal != null && user.getUsername().equals(principal.getName())) {
			return ResponseEntity.ok(new FullUserDTO(user));
		} else {
			return ResponseEntity.ok(new CensoredUserDTO(user));
		}


	}

	@PostMapping("/new")
	public ResponseEntity<FullUserDTO> newUser(@RequestBody User user){ //especificar en la documentación que no se debe poner un campo photo
		if (userService.findByUserName(user.getUsername()).isPresent()) {
			return ResponseEntity.status(409).build(); //409 conflict
		}

		if (!userService.isValidUser(user)){
			return ResponseEntity.badRequest().build();
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
	public ResponseEntity<byte[]> showUserImage(@PathVariable long id){
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
	public ResponseEntity<FullUserDTO> modifyUser(@RequestBody FullUserDTO userDTO, @PathVariable Long id, Principal principal){
		if (userService.findByUserName(userDTO.getUsername()).isPresent()) return ResponseEntity.status(409).build(); //conflict

		User user = new User(userDTO);
		user.setPassword("temporaryPass");
		if (!userService.isValidUser(user)){
			return ResponseEntity.badRequest().build();
		}

		Optional<User> optUser = userService.findById(id);
		if (optUser.isPresent()) {
			if (optUser.get().hasRole("ADMIN")) return ResponseEntity.status(403).build();

			if (checkUserPrivileges(principal, optUser)) return ResponseEntity.status(403).build();

			user.setId(optUser.get().getId());
			user.setEncodedPassword(optUser.get().getEncodedPassword());
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.clearRoles();
			user.setRole("USER");
			user.setId(optUser.get().getId());

			if (user.getPhoto() == null) {
				user.setDefaultPhoto();
			}
			userService.save(user);
			return ResponseEntity.ok(new FullUserDTO(user));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable Long id, Principal principal){
		Optional<User> optUser = userService.findById(id);
		if (checkUserPrivileges(principal, optUser)) return ResponseEntity.status(403).build(); //403 forbidden

		if (optUser.isPresent()) {
			if (optUser.get().hasRole("ADMIN")) return ResponseEntity.status(403).build();
			userService.deleteUserById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private boolean checkUserPrivileges(Principal principal, Optional<User> optUser) {
		if (principal==null) return false;
		Optional<User> authenticatedUser = userService.findByUserName(principal.getName());
		if (authenticatedUser.isPresent()){
			if (!authenticatedUser.get().hasRole("ADMIN") && !principal.getName().equals(optUser.get().getUsername())){
				return true;
			}
		} else {
			return true;
		}
		return false;
	}


}
