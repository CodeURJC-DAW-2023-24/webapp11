package com.EventCrafters.EventCrafters.repository;

import com.EventCrafters.EventCrafters.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);

    @Query(value = "SELECT * FROM eventcrafters.events e where e.category_id = ?1",
            nativeQuery = true)
    List<Event> findByCategory(long categoryId);

    @Query("SELECT e FROM Event e WHERE e.name LIKE %?1%")
    List<Event> findBySearchBar(String input);
    @Query(value = "SELECT * FROM eventcrafters.events e where e.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorId(long id);

    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date < curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorIdPastCreatedEvents(long id);

    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date > curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorIdCurrentCreatedEvents(long id);

    // this gets the id not the events
    @Query(value = "SELECT e.id,e.additional_info, e.attendees_count, e.description, e.end_date, e.location, e.map, e.max_capacity, e.name, e.num_registered_users, e.photo, e.price, e.start_date, e.category_id, e.creator_id FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date < curdate();",
            nativeQuery = true)
    List<Event> findByRegisteredUserIdPastEvents(long id);

    @Query(value = "SELECT e.id,e.additional_info, e.attendees_count, e.description, e.end_date, e.location, e.map, e.max_capacity, e.name, e.num_registered_users, e.photo, e.price, e.start_date, e.category_id, e.creator_id FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date > curdate();",
            nativeQuery = true)
    List<Event> findByRegisteredUserIdCurrentEvents(long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM review WHERE event_id = ?1", nativeQuery = true)
    void deleteReviewsByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM events_registered_users WHERE registered_in_events_id = ?1", nativeQuery = true)
    void deleteEventUserByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM events WHERE id = ?1", nativeQuery = true)
    void deleteEventByIdCustom(Long eventId);


}