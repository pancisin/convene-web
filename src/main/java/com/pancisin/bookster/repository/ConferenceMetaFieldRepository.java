package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.ConferenceMetaField;

public interface ConferenceMetaFieldRepository extends JpaRepository<ConferenceMetaField, Long> {
	
	public List<ConferenceMetaField> findByConferenceId(@Param("conference_id") Long conference_id);
}
