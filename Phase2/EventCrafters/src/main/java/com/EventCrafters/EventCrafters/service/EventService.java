package com.EventCrafters.EventCrafters.service;

import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.EventCrafters.EventCrafters.repository.EventRepository;

@Service
public class EventService {

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

}
