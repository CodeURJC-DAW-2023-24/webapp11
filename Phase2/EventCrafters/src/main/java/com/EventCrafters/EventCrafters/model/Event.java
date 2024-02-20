package com.EventCrafters.EventCrafters.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Blob;
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

	@Lob
	private Blob photo;

	@Column (length = 5000)
	private String description;

	private int maxCapacity;

	private double price;

	private String location;

	private Double latitude;

	private Double longitude;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private String additionalInfo;

	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "creator_id")
	private User creator;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> registeredUsers = new HashSet<>();

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> reviews = new HashSet<>();


	@ManyToOne
	private Category category;
	public Event() {}

	public Event(String name, Blob photo, String description, int maxCapacity, double price, String location,
				 Double latitude, Double longitude, Date startDate, Date endDate, String additionalInfo) {
		this.name = name;
		this.photo = photo;
		this.description = description;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.startDate = startDate;
		this.endDate = endDate;
		this.additionalInfo = additionalInfo;
	}
}