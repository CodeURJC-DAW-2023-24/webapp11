package com.EventCrafters.EventCrafters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;

	private String name;
/*
	@Column(length = 50000)
	private String description;

	public Event() {
	}

	public Event(String nombre, String description) {
		super();
		this.name = nombre;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + name + ", description=" + description + "]";
	}
*/
}
