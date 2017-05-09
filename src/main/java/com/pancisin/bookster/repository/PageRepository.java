package com.pancisin.bookster.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pancisin.bookster.models.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

	@Query("SELECT page FROM Page page")
	public org.springframework.data.domain.Page<Page> getPublic(Pageable pageable);
}
