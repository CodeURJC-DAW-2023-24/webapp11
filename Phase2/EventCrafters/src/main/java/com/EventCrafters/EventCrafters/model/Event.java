package com.EventCrafters.EventCrafters.model;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

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

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> reviews = new HashSet<>();

	@ManyToOne
	private Category category;
	public Event() {}

	public Event(String name, Blob photo, String description, int maxCapacity, double price, String location,
				 String map, Date startDate, Date endDate, String additionalInfo) {
		this.name = name;
		if (photo!=null){
			this.photo = photo;
		} else {
			this.setDefaultPhoto();
		}
		this.description = description;
		this.maxCapacity = maxCapacity;
		this.price = price;
		this.location = location;
		this.map = map;
		this.startDate = startDate;
		this.endDate = endDate;
		this.additionalInfo = additionalInfo;

		this.numRegisteredUsers = 0;
	}

	private void setDefaultPhoto() {
		try {
			ClassPathResource imgFile = new ClassPathResource("static/img/logo.jpeg");
			byte[] photoBytes = new byte[0];
			photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
			this.photo = new SerialBlob(photoBytes);
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int getNumRegisteredUsers() {
		this.numRegisteredUsers = this.getRegisteredUsers().size();
		return numRegisteredUsers;
	}

}