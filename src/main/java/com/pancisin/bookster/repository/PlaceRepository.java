package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

	public Place findByFacebookId(String facebookId);
	
	@Query("SELECT place FROM Page page JOIN page.places place WHERE page.id = :page_id")
	public List<Place> getByPage(@Param("page_id") Long page_id);
	
	@Query("SELECT place FROM Conference conference JOIN conference.places place WHERE conference.id = :conference_id")
	public List<Place> getByConference(@Param("conference_id") Long conference_id);
}
