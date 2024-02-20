package com.EventCrafters.EventCrafters.model;

import java.sql.Blob;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author Lucia and Tarek
 */
@Data
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //mirar estrategia
	private Long id;


	private String name;

	@Column(unique = true, nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String email;

	@Lob
	private Blob photo;

	@JsonIgnore
	@Column(nullable = false)
	private String encodedPassword;


	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	public User() {
	}

	public User(String name,String nickname,String email, Blob photo,String encodedPassword, String... roles) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
		this.photo = photo;
	}

	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Event> createdEvents = new HashSet<>();

	@ManyToMany(mappedBy = "registeredUsers")
	private Set<Event> registeredInEvents = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> reviews = new HashSet<>();

}