package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pancisin.bookster.models.MetaField;

public interface MetaFieldRepository extends JpaRepository<MetaField, Long> {
	
}
