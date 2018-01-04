package com.pancisin.bookster.repository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

//	@Override
//	@Cacheable("events")
//	Event findOne(Long id);
//
//	@Override
//	@CacheEvict(value = "events", key = "#p0.id")
//	<S extends Event> S save(S entity);

	@Query("SELECT event FROM Event event WHERE event.state = 'PUBLISHED' AND DATE(event.date) = DATE(:date)")
	public Page<Event> getPublicByDate(@Param("date") Calendar date, Pageable pageable);

	@Query("SELECT DISTINCT event FROM Event event LEFT JOIN event.page page LEFT JOIN page.administrators administrator LEFT JOIN event.conference conference LEFT JOIN conference.administrators cAdmin WHERE (event.state = 'PUBLISHED' OR event.owner.id = :userId OR administrator.user.id = :userId OR cAdmin.user.id = :userId) AND DATE(event.date) = DATE(:date)")
	public Page<Event> getForUserByDate(@Param("userId") Long userId, @Param("date") Calendar date, Pageable pageable);

	@Query("SELECT DISTINCT event FROM Event event LEFT JOIN event.page page LEFT JOIN page.administrators administrator WHERE page.id = :page_id AND DATE(event.date) >= DATE(:fromDate) AND DATE(event.date) <= DATE(:toDate) AND (event.state = 'PUBLISHED' OR event.owner.id = :userId OR administrator.user.id = :userId)")
	public Page<Event> getByPageRange(@Param("page_id") Long page_id, Pageable pageable,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("userId") Long userId);

	@Query("SELECT DISTINCT event FROM Event event JOIN event.attendees user JOIN event.page page JOIN page.administrators administrator WHERE user.id = :userId AND event.date >= CURDATE() AND (event.state = 'PUBLISHED' OR event.owner = :userId OR administrator.user.id = :userId) ORDER BY event.date ASC")
	public List<Event> getAttending(@Param("userId") Long userId);

	@Query("SELECT event FROM Event event WHERE DATE(event.date) >= DATE(:fromDate) AND DATE(event.date) <= DATE(:toDate) AND event.state = 'PUBLISHED' AND event.featured = 1")
	public Page<Event> getFeaturedEvents(@Param("fromDate") Calendar fromDate, @Param("toDate") Calendar toDate, Pageable pageable);
	// CHECKED LINE

	@Query("SELECT count(event.id) FROM Event event JOIN event.attendees user WHERE user.id = :user_id AND event.id = :event_id")
	public int isAttending(@Param("event_id") Long event_id, @Param("user_id") Long user_id);

	@Query("SELECT event FROM User user JOIN user.events event WHERE user.id = :userId AND (event.state = 'PUBLISHED' OR event.owner.id = :userId) AND DATE(event.date) = DATE(:date) AND event.page IS NULL AND event.conference IS NULL")
	public Page<Event> getPublicCreatedByUser(@Param("userId") Long userId, @Param("date") Calendar date,
			Pageable pageable);

	@Query("SELECT event FROM Event event WHERE (111.045 * DEGREES(ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(event.latitude)) * COS(RADIANS(event.longitude) - RADIANS(:longitude)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(event.latitude))))) < :distance")
	public Page<Event> getEventsByDistance(@Param("latitude") BigDecimal latitude,
			@Param("longitude") BigDecimal longitude, @Param("distance") Double distance, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.owner user WHERE user.id = :user_id AND event.state = 'PUBLISHED' AND event.conference IS NULL AND event.page IS NULL AND DATE(event.date) >= CURDATE()")
	public Page<Event> getByUser(@Param("user_id") Long user_id, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.page page WHERE page.id = :page_id AND DATE(event.date) >= CURDATE()")
	public Page<Event> getByPage(@Param("page_id") Long page_id, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.conference conference WHERE conference.id = :conference_id")
	public Page<Event> getByConference(@Param("conference_id") Long conference_id, Pageable pageable);

	@Query("SELECT event FROM Event event JOIN event.page page WHERE event.id != :event_id AND page.id = (SELECT event.page.id FROM Event event WHERE event.id = :event_id) AND DATE(event.date) >= CURDATE()")
	public Page<Event> getRelated(@Param("event_id") Long event_id, Pageable pageable);

	@Query("SELECT event FROM Event event WHERE event.owner.id = :user_id AND DATE(event.date) >= DATE(:fromDate) AND DATE(event.date) <= DATE(:toDate) AND event.page IS NULL AND event.conference IS NULL")
	public Page<Event> getOwned(@Param("user_id") Long user_id, Pageable pageable, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);
}
