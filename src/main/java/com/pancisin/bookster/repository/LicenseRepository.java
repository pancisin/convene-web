package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.License;

public interface LicenseRepository extends JpaRepository<License, Long> {
	
}
