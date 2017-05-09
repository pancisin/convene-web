package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT event FROM Event event WHERE event.visibility = com.pancisin.bookster.models.enums.Visibility.PUBLIC ORDER BY event.date DESC")
	public Page<Event> getPublic(Pageable pageable);

	@Query("SELECT event FROM Event event WHERE event.owner.id = :user_id AND event.page IS NULL AND event.conference IS NULL")
	public List<Event> getOwned(@Param("user_id") Long user_id);
	
	@Query("SELECT count(event.id) FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.id = :event_id")
	public int isAttending(@Param("event_id") Long event_id, @Param("user_id") Long user_id);
	
	@Query("SELECT event FROM Event event JOIN event.attendees user WHERE user.id = :user_id ORDER BY event.date DESC")
	public List<Event> getAttending(@Param("user_id") Long user_id);

}
