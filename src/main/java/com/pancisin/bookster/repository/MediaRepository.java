package com.pancisin.bookster.repository;

import java.util.List;
import java.util.UUID;

import com.pancisin.bookster.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MediaRepository extends JpaRepository<Media, Long> {

	public Media findById(UUID id);

	@Query("SELECT media FROM Place place JOIN place.gallery media WHERE place.id = :place_id AND media.deleted IS FALSE")
	public List<Media> getByPlace(@Param("place_id") Long place_id);

	@Query("SELECT media FROM Page page JOIN page.gallery media WHERE page.id = :page_id AND media.deleted IS FALSE")
	public List<Media> getByPage(@Param("page_id") Long page_id);

	@Query("SELECT media FROM Event event JOIN event.gallery media WHERE event.id = :event_id AND media.deleted IS FALSE")
	public List<Media> getByEvent(@Param("event_id") Long event_id);

	@Query("SELECT media FROM Media media WHERE media.author.id = :author_id AND media.deleted IS FALSE")
	public List<Media> getByAuthor(@Param("author_id") Long author_id);
}
