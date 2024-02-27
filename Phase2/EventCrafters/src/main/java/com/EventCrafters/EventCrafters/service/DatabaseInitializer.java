package com.EventCrafters.EventCrafters.service;

import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import com.EventCrafters.EventCrafters.repository.ReviewRepository;
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
	private ReviewRepository reviewRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@PostConstruct
	public void init() throws IOException, SQLException {

		Category defaultCategory = new Category("Sin Categoría", "#6c757d");
		categoryRepository.save(defaultCategory);

		ClassPathResource imgFile = new ClassPathResource("static/img/fotoPerfil.jpg");
		byte[] photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob = new SerialBlob(photoBytes);

		ClassPathResource imgFile2 = new ClassPathResource("static/img/fotoPerfil.jpg");
		byte[] photoBytes2 = StreamUtils.copyToByteArray(imgFile2.getInputStream());
		Blob photoBlobuser = new SerialBlob(photoBytes);


		LocalDate startDate = LocalDate.of(2022, 1, 1);
		LocalDate endDate = LocalDate.of(2022, 1, 1);
		LocalDate endDate2 = LocalDate.of(2025, 1, 1);

		Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end2 = Date.from(endDate2.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Event event = new Event("Evento 1", photoBlob, "prueba", 100, 0.00, "Leganes", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end, "blabla");
		Event event2 = new Event("hola", photoBlob, "prueba", 100, 0.00, "Mostoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end2, "blabla");
		Event event3 = new Event("Evento 3", photoBlob, "prueba", 100, 0.00, "Mostoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end, "blabla");
		Event event4 = new Event("Evento 4", photoBlob, "prueba", 100, 0.00, "Mostoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end, "blabla");
		Event event5 = new Event("martillo", photoBlob, "prueba", 100, 0.00, "Mostoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end, "blabla");
		Event event6 = new Event("esternocleidomatoideo", photoBlob, "prueba", 100, 0.00, "Mostoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>", start, end, "blabla");

		User user2 = new User("Juan Usuario","user2","user@gmail.com", photoBlobuser , passwordEncoder.encode("pass"), "USER" );
		userRepository.save(user2);
		User user3 = new User("Juan Usuario","user3","user@gmail.com", photoBlobuser , passwordEncoder.encode("pass"), "USER");
		userRepository.save(user3);
		User user4 = new User("Juan Usuario","user4","user@gmail.com", photoBlobuser , passwordEncoder.encode("pass"), "USER");
		userRepository.save(user4);

		User user = new User("Juan Usuario","user","user@gmail.com", photoBlobuser , passwordEncoder.encode("pass"), "USER");
		userRepository.save(user);
		userRepository.save(new User("Pepe Admin","admin","", photoBlobuser , passwordEncoder.encode("adminpass"), "ADMIN"));

		// categories
		Category deporte = new Category("Deporte", "#cc00ff");
		categoryRepository.save(deporte);
		Category campo = new Category("Campo", "#ffc107");
		categoryRepository.save(campo);
		Category educacion = new Category("Educación", "#28a745");
		categoryRepository.save(educacion);
		event.setCategory(deporte);
		event2.setCategory(campo);
		event3.setCategory(educacion);
		event4.setCategory(educacion);
		event5.setCategory(educacion);
		event6.setCategory(educacion);
		event.setCreator(user);
		event2.setCreator(user);
		event3.setCreator(user);
		event4.setCreator(user);
		event5.setCreator(user);
		event6.setCreator(user);
		eventRepository.save(event);
		eventRepository.save(event2);
		eventRepository.save(event3);
		eventRepository.save(event4);
		eventRepository.save(event5);
		eventRepository.save(event6);

		Review review = new Review(3, "genial");
		review.setUser(user2);
		review.setEvent(event);
		reviewRepository.save(review);

		event.getRegisteredUsers().add(user2);
		event.getRegisteredUsers().add(user3);
		event.getRegisteredUsers().add(user4);
		event2.getRegisteredUsers().add(user2);
		eventRepository.save(event);
		eventRepository.save(event2);

	}

}
