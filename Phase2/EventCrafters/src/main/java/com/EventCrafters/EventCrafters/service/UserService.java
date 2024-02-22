package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public Optional<User> findById(long id) {
		return repository.findById(id);
	}

	public Optional<User> findByUserName(String name) {
		return repository.findByUsername(name);
	}
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<User> findAll() {
		return repository.findAll();
	}

	public void save(User user) {
		repository.save(user);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}

	public Long findUserIdByUsername(String nickname) {
		Optional<User> userOptional = repository.findByName(nickname);
		User user = userOptional.orElse(null);
		return user != null ? user.getId() : null;
	}
	public void deleteUserById(Long userId) {
		repository.deleteById(userId);
	}
}
