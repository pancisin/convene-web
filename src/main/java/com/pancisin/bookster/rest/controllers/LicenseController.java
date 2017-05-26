package com.pancisin.bookster.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasPermission(#license_id, 'license', '')")
@RequestMapping("/api/license/{license_id}")
public class LicenseController {

//	@GetMapping
//	public ResponseEntity<?> getLicense(@PathVariable Long license_id) {
//		return null;
//	}
	

}
