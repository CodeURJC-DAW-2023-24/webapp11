package com.EventCrafters.EventCrafters.repository;

import com.EventCrafters.EventCrafters.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);

    @Query(value = "SELECT * FROM eventcrafters.events e where e.category_id = ?1",
            nativeQuery = true)
    List<Event> findByCategory(long categoryId);

}