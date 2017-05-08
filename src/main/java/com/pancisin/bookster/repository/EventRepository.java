package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pancisin.bookster.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT event FROM Event event WHERE event.visibility = com.pancisin.bookster.models.enums.Visibility.PUBLIC")
	public List<Event> getPublic(Pageable pageable);
}
