package com.pancisin.employger.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/license")
public class LicenseController {

	@RequestMapping("/")
	public ResponseEntity<?> getLicenses() {
		return null;
	}
	
	@RequestMapping("/{license_id}")
	public ResponseEntity<?> getLicenses(@PathVariable Long license_id) {
		return null;
	}
}
