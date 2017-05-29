package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.UserSubscriptionRepository;

@RestController
@PreAuthorize("hasPermission(#license_id, 'license', '')")
@RequestMapping("/api/license/{license_id}")
public class LicenseController {

	@Autowired
	private UserSubscriptionRepository usRepository;
	
	@GetMapping
	public ResponseEntity<?> getLicense(@PathVariable String license_id) {
		return ResponseEntity.ok(usRepository.findById(license_id));
	}
}
