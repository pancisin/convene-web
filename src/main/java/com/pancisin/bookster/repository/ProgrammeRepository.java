package com.pancisin.bookster.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

	@Override
	@CacheEvict(value = "events", key = "#p0.event.id")
	<S extends Programme> S save(S entity);
}
