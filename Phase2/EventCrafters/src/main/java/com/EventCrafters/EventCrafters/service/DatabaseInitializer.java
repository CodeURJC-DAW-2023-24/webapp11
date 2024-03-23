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

		ClassPathResource imgFile = new ClassPathResource("static/img/campo.jpg");
		byte[] photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob1 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/juegosMesa.png");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob2 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/conferencia.jpg");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob3 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/kayak.jpg");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob4 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/torneo.jpg");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob5 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/estrellas.jpg");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlob6 = new SerialBlob(photoBytes);

		imgFile = new ClassPathResource("static/img/fotoPerfil.jpg");
		photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		Blob photoBlobuser = new SerialBlob(photoBytes);


		LocalDate inThePast1Date = LocalDate.of(2022, 2, 1);
		LocalDate inThePast2Date = LocalDate.of(2022, 2, 2);
		LocalDate inTheDistantFuture1Date = LocalDate.of(2025, 3, 1);
		LocalDate inTheDistantFuture2Date = LocalDate.of(2025, 3, 2);
		LocalDate startDate = LocalDate.of(2022, 3, 14);
		LocalDate endDate = LocalDate.of(2022, 3, 14);
		LocalDate endDate2 = LocalDate.of(2025, 10, 3);

		Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end2 = Date.from(endDate2.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date inThePast1 = Date.from(inThePast1Date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date inThePast2 = Date.from(inThePast2Date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date inTheDistantFuture1 = Date.from(inTheDistantFuture1Date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date inTheDistantFuture2 = Date.from(inTheDistantFuture2Date.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Event event = new Event("Paseo por el campo", photoBlob1, "Vive una experiencia sensorial única mientras exploramos la majestuosidad del campo, donde el perfume de las flores se mezcla con risas contagiosas y la serenidad de la naturaleza te envuelve en un abrazo inolvidable.", 150, 0.00, "Leganés", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				start, end, "Es recomendable llevar calzado cómodo.");
		Event event2 = new Event("¡Juegos de mesa!", photoBlob2, "Sumérgete en la emoción estratégica y la camaradería mientras desencadenamos una ola de diversión en nuestro evento de juegos de mesa, donde las risas resuenan entre cartas y dados, creando recuerdos inolvidables en cada movimiento táctico.", 120, 0.00, "Móstoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				end2, end2, "Es recomendable traer juegos de mesa adicionales.");
		Event event3 = new Event("(Ongoing) Conferencia sobre usabilidad", photoBlob3, "Sumérgete en el fascinante mundo de la usabilidad en nuestro evento de conferencia, donde expertos líderes compartirán insights vanguardistas y estrategias prácticas, desatando la innovación para crear experiencias digitales memorables y eficientes.", 100, 0.00, "Móstoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				inThePast1, inTheDistantFuture1, "Ninguna.");
		Event event4 = new Event("(Pending) Ruta en kayak", photoBlob4, "Embárcate en una aventura única en nuestro evento de ruta en kayak, explorando panoramas impresionantes y desafiando tus límites mientras navegas por aguas serenas, creando recuerdos emocionantes en cada remada.", 50, 0.00, "Aranjuez", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				inTheDistantFuture1, inTheDistantFuture2, "Es imprescindible llevar ropa de baño.");
		Event event5 = new Event("(Pending) Torneo de Call of Duty", photoBlob5, "¡Prepárate para la batalla definitiva en nuestro torneo de Call of Duty, donde la emoción se desata y la destreza se enfrenta en un campo de juego virtual, elevando la competencia a un nivel épico que te dejará sin aliento!", 60, 0.00, "Móstoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				inTheDistantFuture1, inTheDistantFuture2, "Se recomienda entrenar antes de ir al torneo.");
		Event event6 = new Event("(Completed) Observación de estrellas", photoBlob6, "Pasa una noche celestial de asombro y descubrimiento en nuestro evento de observación de estrellas, donde el cosmos se despliega ante tus ojos, guiados por astrónomos apasionados que revelarán los secretos del universo bajo el manto estrellado.", 100, 0.00, "Móstoles", "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34410.667558609544!2d-3.7930695448574747!3d40.33120762078209!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4189ed7bdbd7db%3A0x881389995ff67ff3!2sEstadio%20Municipal%20Butarque!5e0!3m2!1ses!2ses!4v1708693009768!5m2!1ses!2ses\" width=\"600\" height=\"450\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>",
				inThePast1, inThePast2, "Pueden llevarse prismáticos o telescopio propios.");

		String mail = "marquesgarciaangel@gmail.com";
		User user2 = new User("Juan Pérez","user2", mail, photoBlobuser, passwordEncoder.encode("pass"), "USER" );
		userRepository.save(user2);
		User user3 = new User("Ana Gómez","user3", mail, photoBlobuser, passwordEncoder.encode("pass"), "USER");
		userRepository.save(user3);
		User user4 = new User("José Ramírez","user4", mail, photoBlobuser, passwordEncoder.encode("pass"), "USER");
		userRepository.save(user4);

		User user = new User("Pedro García","user", mail, photoBlobuser, passwordEncoder.encode("pass"), "USER");
		userRepository.save(user);
		userRepository.save(new User("Pepe Admin","admin","", photoBlobuser, passwordEncoder.encode("adminpass"), "ADMIN"));

		// categories
		Category deporte = new Category("Deporte", "#cc00ff");
		categoryRepository.save(deporte);
		Category campo = new Category("Campo", "#ffc107");
		categoryRepository.save(campo);
		Category educacion = new Category("Educación", "#28a745");
		categoryRepository.save(educacion);
		event.setCategory(campo);
		event2.setCategory(deporte);
		event3.setCategory(educacion);
		event4.setCategory(campo);
		event5.setCategory(deporte);
		event6.setCategory(campo);

		event.setCreator(user);
		event2.setCreator(user);
		event3.setCreator(user2);
		event4.setCreator(user2);
		event5.setCreator(user2);
		event6.setCreator(user2);

		eventRepository.save(event);
		eventRepository.save(event2);
		eventRepository.save(event3);
		eventRepository.save(event4);
		eventRepository.save(event5);
		eventRepository.save(event6);

		Review review = new Review(3, "genial");
		review.setUser(user3);
		review.setEvent(event6);
		reviewRepository.save(review);

		event.getRegisteredUsers().add(user3);
		event.getRegisteredUsers().add(user4);
		event2.getRegisteredUsers().add(user2);
		event6.getRegisteredUsers().add(user);
		event6.getRegisteredUsers().add(user3);
		event6.getRegisteredUsers().add(user4);
		eventRepository.save(event);
		eventRepository.save(event2);
		eventRepository.save(event6);

	}

}
