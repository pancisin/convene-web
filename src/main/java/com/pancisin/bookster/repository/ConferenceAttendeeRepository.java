package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.model.ConferenceAttendee;

public interface ConferenceAttendeeRepository extends JpaRepository<ConferenceAttendee, Long> {

	@Query("SELECT attendee FROM ConferenceAttendee attendee WHERE attendee.conference.id = :conference_id")
	public List<ConferenceAttendee> findByConference(@Param("conference_id") Long conference_id);

	@Query("SELECT attendee FROM ConferenceAttendee attendee WHERE attendee.conference.id = :conference_id AND attendee.user.id = :user_id")
	public ConferenceAttendee findByAttendance(@Param("conference_id") Long conference_id,
			@Param("user_id") Long user_id);
}
