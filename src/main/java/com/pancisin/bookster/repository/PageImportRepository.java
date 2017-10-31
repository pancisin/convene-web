package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.PageImport;

public interface PageImportRepository extends JpaRepository<PageImport, UUID>{
	PageImport findBySourceId(@Param("sourceId") String sourceId);
}
