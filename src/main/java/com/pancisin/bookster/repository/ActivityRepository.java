package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	
	@Query("SELECT activity FROM Page page JOIN page.activities activity WHERE page.id = :page_id")
	public List<Activity> getByPage(@Param("page_id") Long page_id);
	
	@Query("SELECT activity FROM Conference conference JOIN conference.activities activity WHERE conference.id = :conference_id")
	public List<Activity> getByConference(@Param("conference_id") Long conference_id);
}
