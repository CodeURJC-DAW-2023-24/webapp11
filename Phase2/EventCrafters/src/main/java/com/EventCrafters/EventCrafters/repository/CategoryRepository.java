package com.EventCrafters.EventCrafters.repository;

import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT name FROM eventcrafters.categories",
            nativeQuery = true)
    List<String> findAllNames();

    @Query(value = "SELECT COUNT(eventcrafters.events.id) AS event_count FROM eventcrafters.categories LEFT JOIN eventcrafters.events ON eventcrafters.categories.id = eventcrafters.events.category_id GROUP BY eventcrafters.categories.id, eventcrafters.categories.name;",
            nativeQuery = true)
    List<Integer> findAllCategoriesUsedCount();

    @Query(value = "SELECT eventcrafters.categories.id FROM eventcrafters.categories LEFT JOIN eventcrafters.events ON eventcrafters.categories.id = eventcrafters.events.category_id GROUP BY eventcrafters.categories.id, eventcrafters.categories.name;",
            nativeQuery = true)
    List<Integer> findAllCategoriesUsedId();



}
