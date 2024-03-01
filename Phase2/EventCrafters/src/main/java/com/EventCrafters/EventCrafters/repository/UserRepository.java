package com.EventCrafters.EventCrafters.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT e.id FROM eventcrafters.events e " +
            "JOIN eventcrafters.categories c ON e.category_id = c.id " +
            "JOIN (SELECT c.id AS category_id, COUNT(DISTINCT CASE WHEN eru.registered_users_id = ?1 THEN eru.registered_users_id ELSE NULL END) AS count " +
            "FROM eventcrafters.categories c " +
            "LEFT JOIN eventcrafters.events e ON c.id = e.category_id " +
            "LEFT JOIN eventcrafters.events_registered_users eru ON e.id = eru.registered_in_events_id AND eru.registered_users_id = ?1 " +
            "GROUP BY c.id) AS agg ON c.id = agg.category_id " +
            "ORDER BY agg.count DESC, e.category_id;", nativeQuery = true)
    List<Long> getUserCategoryPreferences(Long userId);

    @Query(value = "SELECT e.id FROM eventcrafters.events e " +
            "JOIN eventcrafters.categories c ON e.category_id = c.id " +
            "JOIN (SELECT c.id AS category_id, COUNT(DISTINCT CASE WHEN eru.registered_users_id = ?1 THEN eru.registered_users_id ELSE NULL END) AS count " +
            "FROM eventcrafters.categories c " +
            "LEFT JOIN eventcrafters.events e ON c.id = e.category_id " +
            "LEFT JOIN eventcrafters.events_registered_users eru ON e.id = eru.registered_in_events_id AND eru.registered_users_id = ?1 " +
            "GROUP BY c.id) AS agg ON c.id = agg.category_id " +
            "ORDER BY agg.count DESC, e.category_id",
            countQuery = "SELECT COUNT(DISTINCT e.id) FROM eventcrafters.events e " +
                    "JOIN eventcrafters.categories c ON e.category_id = c.id " +
                    "JOIN (SELECT c.id AS category_id, COUNT(DISTINCT CASE WHEN eru.registered_users_id = ?1 THEN eru.registered_users_id ELSE NULL END) AS count " +
                    "FROM eventcrafters.categories c " +
                    "LEFT JOIN eventcrafters.events e ON c.id = e.category_id " +
                    "LEFT JOIN eventcrafters.events_registered_users eru ON e.id = eru.registered_in_events_id AND eru.registered_users_id = ?1 " +
                    "GROUP BY c.id) AS agg ON c.id = agg.category_id",
            nativeQuery = true)
    Page<BigInteger> getUserCategoryPreferences(Long userId, Pageable pageable);
}
