package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.json.ConferenceUserWrapper;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

	@Query("SELECT DISTINCT conference FROM Conference conference LEFT JOIN conference.invitations invitation LEFT JOIN conference.conferenceAdministrators administrator WHERE (conference.visibility = 'PUBLIC' OR invitation.user.id = :user_id OR administrator.user.id = :user_id)")
	public Page<Conference> getForUser(@Param("user_id") Long user_id, Pageable pageable);
}
