package com.EventCrafters.EventCrafters.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.repository.EventRepository;

@Service
public class EventService {

	private int nextEventIndex = 3;
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

	public void delete(long id) {
		repository.deleteById(id);
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

}
