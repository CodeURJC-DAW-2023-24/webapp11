package com.EventCrafters.EventCrafters.service;

import javax.annotation.PostConstruct;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.repository.EventRepository;
import com.EventCrafters.EventCrafters.repository.UserRepository;

import java.sql.Blob;
import java.util.Date;

@Service
public class DatabaseInitializer {


	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@PostConstruct
	public void init() {

		eventRepository.save(new Event("Evento 1", null, "prueba", 100, 0.00, "Mostoles", 145.234, 345678.34, new Date(2022, 0, 1), new Date(2022, 0, 1), "blabla"));

		userRepository.save(new User("user","user1","", null, passwordEncoder.encode("pass"), "USER"));
		userRepository.save(new User("admin","admin1","", null, passwordEncoder.encode("adminpass"), "USER", "ADMIN"));

		// categories
		categoryRepository.save(new Category("campo", "#ffc107"));
		categoryRepository.save(new Category("deporte", "#cc00ff"));
		categoryRepository.save(new Category("educación", "#28a745"));
	}

}
