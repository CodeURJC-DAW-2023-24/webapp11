package com.EventCrafters.EventCrafters.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
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

	@Getter
	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Lob
	private Blob photo;

	@JsonIgnore
	@Column(nullable = false)
	private String encodedPassword;


	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;

	public User() {
	}

	public User(String name, String username, String email, Blob photo, String encodedPassword, String... roles) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
		this.photo = photo;
	}

	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Event> createdEvents = new HashSet<>();

	@ManyToMany(mappedBy = "registeredUsers", cascade = CascadeType.ALL)
	private Set<Event> registeredInEvents = new HashSet<>();


	public String getPassword() {
		return encodedPassword;
	}

	public void setPassword(String password) {
		this.encodedPassword = password;
	}

	public void setRole(String roleUser) {

		if (this.roles == null){
			this.roles =  new ArrayList<>();
		}
		this.roles.add(roleUser);
	}

	public Long getId() {
		return id;
	}
}