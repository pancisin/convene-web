package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT event FROM Event event WHERE event.visibility = com.pancisin.bookster.models.enums.Visibility.PUBLIC AND event.date >= CURDATE() ORDER BY event.date ASC")
	public Page<Event> getPublic(Pageable pageable);

	@Query("SELECT event FROM Event event WHERE event.owner.id = :user_id AND event.page IS NULL AND event.conference IS NULL ORDER BY date ASC")
	public List<Event> getOwned(@Param("user_id") Long user_id);
	
	@Query("SELECT count(event.id) FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.id = :event_id")
	public int isAttending(@Param("event_id") Long event_id, @Param("user_id") Long user_id);
	
	@Query("SELECT event FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.date >= CURDATE() ORDER BY event.date ASC")
	public List<Event> getAttending(@Param("user_id") Long user_id);

}
