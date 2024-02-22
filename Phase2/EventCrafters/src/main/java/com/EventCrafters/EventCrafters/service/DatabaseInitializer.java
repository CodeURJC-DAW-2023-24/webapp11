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
		Event event = new Event("Evento 1", photoBlob, "prueba", 100, 0.00, "Mostoles", 145.234,345678.34, start, end, "blabla");
		Event event2 = new Event("Evento 2", photoBlob, "prueba", 100, 0.00, "Mostoles", 145.234,345678.34, start, end, "blabla");
		Event event3 = new Event("Evento 3", photoBlob, "prueba", 100, 0.00, "Mostoles", 145.234,345678.34, start, end, "blabla");

		User user = new User("Juan Usuario","user","user@gmail.com", null, passwordEncoder.encode("pass"), "USER");
		userRepository.save(user);
		userRepository.save(new User("Pepe Admin","admin","", null, passwordEncoder.encode("adminpass"), "ADMIN"));

		// categories
		Category deporte = new Category("Deporte", "#cc00ff");
		categoryRepository.save(deporte);
		Category campo = new Category("Campo", "#ffc107");
		categoryRepository.save(campo);
		Category educación = new Category("Educación", "#28a745");
		categoryRepository.save(educación);
		event.setCategory(deporte); // Asocia el evento con la categoría "campo"
		event2.setCategory(campo);
		event3.setCategory(educación);
		event.setCreator(user);
		event2.setCreator(user);
		event3.setCreator(user);
		eventRepository.save(event);
		eventRepository.save(event2);
		eventRepository.save(event3);
	}

}
