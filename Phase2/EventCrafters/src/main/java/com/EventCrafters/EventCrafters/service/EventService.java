package com.EventCrafters.EventCrafters.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

	@Autowired
	private MailService mailService;

	@Autowired
	private EventRepository repository;

	public Optional<Event> findById(long id) {
		return repository.findById(id);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Event> findAll() { return repository.findAll(); }
	public List<Event> findAll(int page,int pageSize) {
		return repository.findAll(PageRequest.of(page, pageSize)).getContent();
	}

	public Event save(Event event) {
		return repository.save(event);
	}

	public Event update(Event event){
		Set<User> registeredUsers = event.getRegisteredUsers();
		String subject = "Actualización del Evento: " + event.getName();
		for (User user : registeredUsers) {
			String content = generateUpdateEmailContent(event.getName());
			mailService.sendEmail(user, subject, content, true);
		}

		return repository.save(event);
	}

	private String generateUpdateEmailContent(String eventName) {
		return String.format(
				"<html>" +
						"<head>" +
						"<style>" +
						"body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }" +
						".container { background-color: #f8f8f8; border-radius: 10px; padding: 20px; text-align: center; }" +
						"h2 { color: #4CAF50; }" +
						"p { margin: 10px 0; }" +
						"</style>" +
						"</head>" +
						"<body>" +
						"<div class='container'>" +
						"<h2>Uno de los eventos en los que estás inscrito ha sido modificado</h2>" +
						"<p>El evento '%s' ha recibido importantes actualizaciones.</p>" +
						"<p>Te invitamos a iniciar sesión en tu cuenta para descubrir todas las novedades.</p>" +
						"</div>" +
						"</body>" +
						"</html>", eventName);
	}

	@Transactional
	public void delete(Long eventId) {
		Optional<Event> eventOptional = repository.findById(eventId);
		Date now = new Date();
		if(eventOptional.isPresent()) {
			Event event = eventOptional.get();
			if(event.getStartDate().after(now)) {
				for (User notifiedUser : event.getRegisteredUsers()) {
					String subject = "Información Importante Sobre Tu Evento Inscrito";
					String content = generateEmailContent(event.getName());
					mailService.sendEmail(notifiedUser, subject, content, true);
				}
			}
		}
		repository.deleteReviewsByEventId(eventId);
		repository.deleteEventUserByEventId(eventId);
		repository.deleteEventByIdCustom(eventId);

	}

	private String generateEmailContent(String eventName) {
		return String.format(
				"<html>" +
						"<head>" +
						"<style>" +
						"body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }" +
						".container { background-color: #f8f8f8; border: 1px solid #ddd; padding: 20px; }" +
						"h2 { color: #f40; }" +
						"p { margin: 10px 0; }" +
						"</style>" +
						"</head>" +
						"<body>" +
						"<div class='container'>" +
						"<h2>Evento Cancelado: '%s'</h2>" +
						"<p>Querido participante,</p>" +
						"<p>Lamentamos informarte que el evento '%s', en el cual estabas inscrito, ha sido cancelado.</p>" +
						"<p>Entendemos que esto puede ser decepcionante y agradecemos tu comprensión. Estamos comprometidos a ofrecerte la mejor experiencia y te invitamos a explorar otros eventos emocionantes en nuestra plataforma.</p>" +
						"<p>Gracias por tu apoyo continuo.</p>" +
						"</div>" +
						"</body>" +
						"</html>", eventName, eventName);
	}

	public List<Event> findByCategory(long id) {return repository.findByCategory(id);}

	public List<Event> findByCategory(long id, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByCategory(id, pageable).getContent();
	}

	public List<Event> findBySearchBar(String input) {return repository.findBySearchBar(input);}

	public List<Event> findBySearchBar(String input, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findBySearchBar(input, pageable).getContent();
	}

	public List<Event> findByCreatorIdCurrentCreatedEvents(Long id){return repository.findByCreatorIdCurrentCreatedEvents(id);}

	public List<Event> findByCreatorIdCurrentCreatedEvents(Long id, int page, int  pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByCreatorIdCurrentCreatedEvents(id, pageable).getContent();
	}
	public List<Event> findByCreatorIdPastCreatedEvents(Long id) {return repository.findByCreatorIdPastCreatedEvents(id);}

	public List<Event> findByCreatorIdPastCreatedEvents(Long id, int page, int  pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByCreatorIdPastCreatedEvents(id, pageable).getContent();
	}

	public List<Event> findByRegisteredUserIdCurrentEvents(Long id) {return repository.findByRegisteredUserIdCurrentEvents(id);}

	public List<Event> findByRegisteredUserIdCurrentEvents(Long id, int page, int  pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByRegisteredUserIdCurrentEvents(id, pageable).getContent();
	}

	public List<Event> findByRegisteredUserIdPastEvents(Long id) {return repository.findByRegisteredUserIdPastEvents(id);}

	public List<Event> findByRegisteredUserIdPastEvents(Long id, int page, int  pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByRegisteredUserIdPastEvents(id, pageable).getContent();
	}

	@Transactional
	public void updateAttendeesCount(Long eventId, int attendeesCount) {
		Event event = repository.findById(eventId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventId));
		event.setAttendeesCount(attendeesCount);
		repository.save(event);
	}

	public List<Event> eventsOrderedByPopularity(){
		return repository.findByRegisteredUsersCount();
	}

	public List<Event> eventsOrderedByPopularity(int page, int pageSize){
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByRegisteredUsersCount(pageable).getContent();
	}

	public String getShortDescription(String desc) {
		int limit = 40;
		if (desc.length() > limit) {
			return desc.substring(0, limit - 3) + "...";
		} else {
			return desc;
		}
	}

	public String formatDate(Date date) {
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(date);
	}
}
