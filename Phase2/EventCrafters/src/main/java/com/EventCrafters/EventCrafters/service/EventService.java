package com.EventCrafters.EventCrafters.service;

import java.util.*;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {


	private List<List<Event>> allEvents;
	private List<Integer> nextEventIndex;
	private int eventsRefreshSize = 3;

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




	public EventService() {
		this.allEvents = new ArrayList<>();
		this.nextEventIndex = new ArrayList<>();
		int i;
		int max = 5;
		for (i = 0; i<max; i++){
			this.allEvents.add(new ArrayList<>());
			this.nextEventIndex.add(0);
		}
	}


	@Transactional
	public void delete(Long eventId) {
		repository.deleteReviewsByEventId(eventId);
		repository.deleteEventUserByEventId(eventId);
		repository.deleteEventByIdCustom(eventId);
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

	public List<Event> findAjax(){
		return findAjax(-1L,0);
	}

	public List<Event> findAjax(Long id, int i){
		switch (i){
			case 0 :
				this.allEvents.set(i, repository.findAll());
				break;
			case 1 :
				this.allEvents.set(i, repository.findByCreatorIdCurrentCreatedEvents(id));
				break;
			case 2 :
				this.allEvents.set(i, repository.findByCreatorIdCurrentCreatedEvents(id));
				break;
			case 3:
				this.allEvents.set(i, repository.findByRegisteredUserIdCurrentEvents(id));
				break;
			case 4:
				this.allEvents.set(i, repository.findByRegisteredUserIdPastEvents(id));
				break;
		}
		this.nextEventIndex.set(i, this.eventsRefreshSize);
		if (allEvents.get(i).isEmpty()){
			return new ArrayList<>();
		}else if (allEvents.get(i).size() <= nextEventIndex.get(i)){
			return allEvents.get(i).subList(0,allEvents.get(i).size());
		}
		return allEvents.get(i).subList(0,this.eventsRefreshSize);
	}

	public int getNextEventIndex(int i) {
		return nextEventIndex.get(i);
	}

	public int getEventsRefreshSize() {
		return eventsRefreshSize;
	}

	public List<Event> getAllEvents(int i) {
		return allEvents.get(i);
	}


	public void setNextEventIndex(int i, int nextEventIndex) {
		this.nextEventIndex.set(i, nextEventIndex);
	}

	@Transactional
	public void updateAttendeesCount(Long eventId, int attendeesCount) {
		Event event = repository.findById(eventId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventId));
		event.setAttendeesCount(attendeesCount);
		repository.save(event);
	}
}
