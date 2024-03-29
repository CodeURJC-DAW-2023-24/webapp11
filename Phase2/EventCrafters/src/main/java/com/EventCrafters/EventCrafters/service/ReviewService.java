package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Event;
import com.EventCrafters.EventCrafters.model.Review;
import com.EventCrafters.EventCrafters.model.User;
import com.EventCrafters.EventCrafters.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;

	public Optional<Review> findById(long id) {
		return repository.findById(id);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Review> findAll() {
		return repository.findAll();
	}

	public void save(Review review) {
		repository.save(review);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}

	public double calculateAverageRatingForEvent(Long eventId) {
		return repository.findAverageRatingByEvent(eventId)
				.orElse(0.0);
	}

	public boolean hasUserReviewedEvent(Long eventId, Long Id) {
		Optional<Review> review = repository.findByEventIdAndUserId(eventId, Id);
		return review.isPresent();
	}

	public Optional<Review> findByUserAndEvent(User user, Event event) {
		return repository.findByEventIdAndUserId(user.getId(), event.getId());
	}

	public int countReviewsForEvent(Long eventId) {
		return repository.countByEventId(eventId);
	}
}
