package com.EventCrafters.EventCrafters.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EventCrafters.EventCrafters.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String nickname);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM events_registered_users WHERE registered_users_id = ?1", nativeQuery = true)
    void deleteRegistrationsByUserId(Long userId);


}
