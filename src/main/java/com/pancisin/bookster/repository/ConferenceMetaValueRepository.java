package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.ConferenceMetaValue;

@Repository
public interface ConferenceMetaValueRepository extends JpaRepository<ConferenceMetaValue, Long> {
	
	@Query("SELECT value FROM ConferenceMetaValue value JOIN value.field field JOIN field.conference conf WHERE conf.id = :conference_id AND value.user.id = :user_id")
	public List<ConferenceMetaValue> getByConference(@Param("user_id") Long user_id, @Param("conference_id") Long conference_id);
	
	@Query("SELECT value FROM ConferenceMetaValue value JOIN value.field field WHERE field.conference.id = :conference_id")
	public List<ConferenceMetaValue> getByConference(@Param("conference_id") Long conference_id);
}
