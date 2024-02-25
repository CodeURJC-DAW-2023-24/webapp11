package com.EventCrafters.EventCrafters.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {


	private List<Event> allEvents;
	private int nextEventIndex;
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
		this.allEvents = repository.findAll();
		this.nextEventIndex = this.eventsRefreshSize;
		if (allEvents.isEmpty()){
			return new ArrayList<>();
		}else if (allEvents.size() <= nextEventIndex){
			return allEvents.subList(0,allEvents.size());
		}
		return allEvents.subList(0,this.eventsRefreshSize);
	}

	public int getNextEventIndex() {
		return nextEventIndex;
	}

	public int getEventsRefreshSize() {
		return eventsRefreshSize;
	}

	public List<Event> getAllEvents() {
		return allEvents;
	}

	public void setNextEventIndex(int nextEventIndex) {
		this.nextEventIndex = nextEventIndex;
	}

}
