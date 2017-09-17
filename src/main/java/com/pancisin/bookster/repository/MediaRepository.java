package com.pancisin.bookster.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {

	public Media findById(UUID id);
	
	@Query("SELECT media FROM Place place JOIN place.gallery media WHERE place.id = :place_id AND media.deleted = 0")
	public List<Media> getByPlace(@Param("place_id") Long place_id);
}
