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

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;


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
	public void init() throws IOException, SQLException {

		ClassPathResource imgFile = new ClassPathResource("static/img/fotoPerfil.jpg");
		byte[] photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob = new javax.sql.rowset.serial.SerialBlob(photoBytes);

		LocalDate startDate = LocalDate.of(2022, 1, 1);
		LocalDate endDate = LocalDate.of(2022, 1, 1);
		Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Event event = new Event("Evento 1", photoBlob, "prueba", 100, 0.00, "Mostoles", 145.234, 345678.34, start, end, "blabla");
		eventRepository.save(event);

		userRepository.save(new User("Juan Usuario","user","", null, passwordEncoder.encode("pass"), "USER"));
		userRepository.save(new User("Pepe Admin","admin","", null, passwordEncoder.encode("adminpass"), "ADMIN"));

		// categories
		Category deporte = new Category("Deporte", "#cc00ff");
		categoryRepository.save(deporte);
		categoryRepository.save(new Category("Campo", "#ffc107"));
		categoryRepository.save(new Category("Educación", "#28a745"));
		event.setCategory(deporte); // Asocia el evento con la categoría "campo"
		eventRepository.save(event);
	}

}
