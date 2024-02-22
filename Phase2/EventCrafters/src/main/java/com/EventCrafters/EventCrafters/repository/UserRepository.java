package com.EventCrafters.EventCrafters.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EventCrafters.EventCrafters.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String nickname);

}
