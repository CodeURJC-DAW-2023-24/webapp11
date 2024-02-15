package com.EventCrafters.EventCrafters.repository;

import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

}
