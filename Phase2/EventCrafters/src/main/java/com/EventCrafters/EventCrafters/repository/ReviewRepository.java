package com.EventCrafters.EventCrafters.repository;

import java.util.List;
import java.util.Optional;

import com.EventCrafters.EventCrafters.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.event.id = :eventId")
    Optional<Double> findAverageRatingByEvent(Long eventId);

    @Query("SELECT r FROM Review r WHERE r.event.id = :eventId AND r.user.id = :userId")
    Optional<Review> findByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.user.id = ?1")
    void deleteReviewsByUserId(Long userId);

}
