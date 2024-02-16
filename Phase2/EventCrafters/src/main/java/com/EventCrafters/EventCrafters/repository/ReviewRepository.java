package com.EventCrafters.EventCrafters.repository;

import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //Optional<Review> findByName(String name);

}
