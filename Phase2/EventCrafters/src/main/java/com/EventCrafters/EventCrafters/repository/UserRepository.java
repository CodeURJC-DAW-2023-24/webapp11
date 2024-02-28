package com.EventCrafters.EventCrafters.repository;

import java.util.List;
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

    @Query(value = "SELECT category_id, COUNT(e.category_id) AS count FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id WHERE eru.registered_users_id = ?1 GROUP BY e.category_id ORDER BY count DESC;", nativeQuery = true)
    List<Object[]> getUserCategoryPreferences(Long userId);
}
