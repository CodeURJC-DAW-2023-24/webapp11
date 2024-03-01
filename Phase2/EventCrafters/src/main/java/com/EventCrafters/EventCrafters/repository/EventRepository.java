package com.EventCrafters.EventCrafters.repository;

import com.EventCrafters.EventCrafters.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.EventCrafters.EventCrafters.model.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);

    @Query(value = "SELECT * FROM eventcrafters.events e where e.category_id = ?1",
            nativeQuery = true)
    List<Event> findByCategory(long categoryId);

    @Query(value = "SELECT * FROM eventcrafters.events e where e.category_id = ?1",
            countQuery = "SELECT count(*) FROM eventcrafters.events e where e.category_id = ?1",
            nativeQuery = true)
    Page<Event> findByCategory(long categoryId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.name LIKE %?1%")
    List<Event> findBySearchBar(String input);

    @Query("SELECT e FROM Event e WHERE e.name LIKE CONCAT('%', :input, '%')")
    Page<Event> findBySearchBar(@Param("input") String input, Pageable pageable);
    @Query(value = "SELECT * FROM eventcrafters.events e where e.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorId(long id);

    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date < curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorIdPastCreatedEvents(long id);

    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date < curdate() and eventcrafters.events.creator_id = ?1",
            countQuery = "SELECT count(*) FROM eventcrafters.events where eventcrafters.events.end_date < curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    Page<Event> findByCreatorIdPastCreatedEvents(long id, Pageable pageable);

    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date > curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    List<Event> findByCreatorIdCurrentCreatedEvents(long id);


    @Query(value = "SELECT * FROM eventcrafters.events where eventcrafters.events.end_date > curdate() and eventcrafters.events.creator_id = ?1",
            countQuery = "SELECT count(*) FROM eventcrafters.events where eventcrafters.events.end_date > curdate() and eventcrafters.events.creator_id = ?1",
            nativeQuery = true)
    Page<Event> findByCreatorIdCurrentCreatedEvents(long id, Pageable pageable);

    @Query(value = "SELECT e.* FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date < curdate();",
            nativeQuery = true)
    List<Event> findByRegisteredUserIdPastEvents(long id);

    @Query(value = "SELECT e.* FROM eventcrafters.events_registered_users eru " +
            "JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id " +
            "WHERE eru.registered_users_id = ?1 AND e.end_date < curdate()",
            countQuery = "SELECT count(e.id) FROM eventcrafters.events_registered_users eru " +
                    "JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id " +
                    "WHERE eru.registered_users_id = ?1 AND e.end_date < curdate()",
            nativeQuery = true)
    Page<Event> findByRegisteredUserIdPastEvents(long userId, Pageable pageable);

    @Query(value = "SELECT e.* FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date > curdate();",
            nativeQuery = true)
    List<Event> findByRegisteredUserIdCurrentEvents(long id);

    @Query(value = "SELECT e.* FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date > curdate()",
            countQuery = "SELECT count(e.id) FROM eventcrafters.events_registered_users eru JOIN eventcrafters.events e ON eru.registered_in_events_id = e.id where eru.registered_users_id = ?1 and e.end_date > curdate()",
            nativeQuery = true)
    Page<Event> findByRegisteredUserIdCurrentEvents(long id, Pageable pageable);

    @Query(value = "SELECT e.* FROM eventcrafters.events e left join eventcrafters.events_registered_users eru on  e.id = eru.registered_in_events_id group by e.id order by count(eru.registered_users_id) desc;",
            nativeQuery = true)
    List<Event> findByRegisteredUsersCount();

    @Query(value = "SELECT e.* FROM eventcrafters.events e left join eventcrafters.events_registered_users eru on  e.id = eru.registered_in_events_id group by e.id order by count(eru.registered_users_id) desc",
            countQuery = "SELECT count(*) FROM eventcrafters.events e left join eventcrafters.events_registered_users eru on  e.id = eru.registered_in_events_id group by e.id",
            nativeQuery = true)
    Page<Event> findByRegisteredUsersCount(Pageable pageable);


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

    List<Event> findByCreatorId(Long creatorId);
}