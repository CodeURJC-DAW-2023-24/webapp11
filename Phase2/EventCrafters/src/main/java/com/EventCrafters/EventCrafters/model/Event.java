package com.EventCrafters.EventCrafters.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Blob;

import lombok.AccessLevel;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Lucia and Tarek
 */

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int attendeesCount = -1;

	@Lob
	private Blob photo;

	@Column (length = 5000)
	private String description;

	private int maxCapacity;

	private double price;

	private String location;

	@Column (length = 5000)
	private String map;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private String additionalInfo;

	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private User creator;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> registeredUsers = new HashSet<>();

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private int numRegisteredUsers;

	private String formattedStartDate;
	private String formattedEndDate;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> reviews = new HashSet<>();


	@ManyToOne
	private Category category;
	public Event() {}

	public Event(String name, Blob photo, String description, int maxCapacity, double price, String location,
				 String map, Date startDate, Date endDate, String additionalInfo) {
		this.name = name;
		this.photo = photo;
		this.description = description;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.location = location;
		this.map = map;
		this.startDate = startDate;
		this.endDate = endDate;
		this.additionalInfo = additionalInfo;
		this.numRegisteredUsers = 0;
		this.formattedStartDate = formatDate(startDate);
		this.formattedEndDate = formatDate(endDate);
	}

	public int getNumRegisteredUsers() {
		this.numRegisteredUsers = this.getRegisteredUsers().size();
		return numRegisteredUsers;
	}

	public String formatDate(Date date) {
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(date);
	}
}