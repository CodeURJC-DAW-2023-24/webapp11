package com.EventCrafters.EventCrafters.controller;

import java.util.Collection;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.EventCrafters.EventCrafters.service.EventService;

@RestController
@RequestMapping("/api/books")
public class EventRestController {
/*
	@Autowired
	private EventService service;

	@GetMapping("/")
	public Collection<Event> getBooks() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Event> getBook(@PathVariable long id) {

		Optional<Event> op = service.findById(id);
		if (op.isPresent()) {
			Event event = op.get();
			return new ResponseEntity<>(event, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Event createBook(@RequestBody Event event) {

		service.save(event);

		return event;
	}

	@PutMapping("/{id}")
	public ResponseEntity<Event> updateBook(@PathVariable long id, @RequestBody Event updatedEvent) {

		if (service.exist(id)) {

			updatedEvent.setId(id);
			service.save(updatedEvent);

			return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Event> deleteBook(@PathVariable long id) {

		try {
			service.delete(id);
			return new ResponseEntity<>(null, HttpStatus.OK);

		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	*/
}
