package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.License;

public interface LicenseRepository extends JpaRepository<License, Long> {
	
}
