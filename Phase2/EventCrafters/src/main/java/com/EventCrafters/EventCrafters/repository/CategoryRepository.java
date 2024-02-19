package com.EventCrafters.EventCrafters.repository;

import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {



}
