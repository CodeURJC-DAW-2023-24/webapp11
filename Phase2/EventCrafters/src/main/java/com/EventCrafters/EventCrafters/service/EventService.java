package com.EventCrafters.EventCrafters.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

	public List<Event> findAll() {
		return repository.findAll();
	}

	public void save(Event event) {
		repository.save(event);
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

	public List<Event> findBySearchBar(String input) {return repository.findBySearchBar(input);}

	public AbstractMap.SimpleEntry<List<Event>, Integer> getAdditionalEvents(List<Event> list, int nextEventIndex, int eventsRefreshSize) {
		List<Event> additionalEvents = new ArrayList<>();
		if (list.size() <= nextEventIndex){
			additionalEvents = list.subList(0,list.size());
			nextEventIndex = list.size();
		}
		else{
			additionalEvents = list.subList(0,nextEventIndex);
		}
		return new AbstractMap.SimpleEntry<List<Event>, Integer>(additionalEvents, nextEventIndex);
	}

	public List<Event> findByCreatorIdCurrentCreatedEvents(Long id) {return repository.findByCreatorIdCurrentCreatedEvents(id);}
	public List<Event> findByCreatorIdPastCreatedEvents(Long id) {return repository.findByCreatorIdPastCreatedEvents(id);}
	public List<Event> findByRegisteredUserIdCurrentEvents(Long id) {return repository.findByRegisteredUserIdCurrentEvents(id);}
	public List<Event> findByRegisteredUserIdPastEvents(Long id) {return repository.findByRegisteredUserIdPastEvents(id);}


	@Transactional
	public void updateAttendeesCount(Long eventId, int attendeesCount) {
		Event event = repository.findById(eventId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventId));
		event.setAttendeesCount(attendeesCount);
		repository.save(event);
	}

	public List<Event> eventsOrderedByPopularity(){
		List<Object[]> l = repository.findByRegisteredUsersCount();
		List<Event> result = new ArrayList<>();
		for (Object[] element : l){
			BigInteger aux = (BigInteger) element[0];
			Optional<Event> e = this.findById(aux.longValue());

			// if e is present then add it to de list
            e.ifPresent(result::add);
		}
		for (Event e : this.findAll()){
			if (!result.contains(e)){
				result.add(e);
			}
		}

		return result;
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
