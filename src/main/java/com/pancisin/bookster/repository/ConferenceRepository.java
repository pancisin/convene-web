package com.pancisin.bookster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

	@Query("SELECT DISTINCT conference FROM Conference conference LEFT JOIN conference.invitations invitation LEFT JOIN conference.conferenceAdministrators administrator " + 
	"WHERE ((conference.visibility = 'PUBLIC' OR invitation.user.id = :user_id) AND (conference.state = 'PUBLISHED' OR conference.state = 'BLOCKED')) OR administrator.user.id = :user_id")
	public Page<Conference> getForUser(@Param("user_id") Long user_id, Pageable pageable);

	@Query("SELECT conference FROM Conference conference WHERE conference.visibility = 'PUBLIC' AND (conference.state = 'PUBLISHED' OR conference.state = 'BLOCKED')")
	public Page<Conference> getPublic(Pageable pageable);

	@Query("SELECT conference FROM Conference conference WHERE conference.id = :conference_id AND conference.visibility = 'PUBLIC' AND (conference.state = 'PUBLISHED' OR conference.state = 'BLOCKED')")
	public Conference getPublicConference(@Param("conference_id") Long conference_id);
}
