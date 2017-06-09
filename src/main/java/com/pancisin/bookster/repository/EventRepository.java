package com.pancisin.bookster.repository;

import java.math.BigDecimal;
import java.util.Calendar;
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

	@Query("SELECT event FROM Event event WHERE event.owner.id = :user_id AND event.page IS NULL AND event.conference IS NULL")
	public Page<Event> getOwned(@Param("user_id") Long user_id, Pageable pageable);

	@Query("SELECT count(event.id) FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.id = :event_id")
	public int isAttending(@Param("event_id") Long event_id, @Param("user_id") Long user_id);

	@Query("SELECT event FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.date >= CURDATE() ORDER BY event.date ASC")
	public List<Event> getAttending(@Param("user_id") Long user_id);

	@Query("SELECT event FROM Event event WHERE event.visibility = com.pancisin.bookster.models.enums.Visibility.PUBLIC AND event.date = :date")
	public Page<Event> getPublicByDate(@Param("date") Calendar date, Pageable pageable);
	
	@Query("SELECT event FROM Event event RIGHT JOIN event.place place JOIN place.address address WHERE (111.045 * DEGREES(ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(address.latitude)) * COS(RADIANS(address.longitude) - RADIANS(:longitude)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(address.latitude))))) < :distance")
	public Page<Event> getEventsByDistance(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude, @Param("distance") Double distance, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.owner user WHERE user.id = :user_id AND event.visibility = 'PUBLIC' AND event.conference IS NULL AND event.page IS NULL")
	public List<Event> getEventsByUser(@Param("user_id") Long user_id);
	
	@Query("SELECT event FROM Event event JOIN event.page page WHERE page.id = :page_id")
	public Page<Event> getByPage(@Param("page_id") Long page_id, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.conference conference WHERE conference.id = :conference_id")
	public Page<Event> getByConference(@Param("conference_id") Long conference_id, Pageable pageable);
}
