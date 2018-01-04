package com.pancisin.bookster.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.model.enums.PageRole;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

	@GetMapping
	public ResponseEntity<?> getRoles() {
		return ResponseEntity.ok(PageRole.values());
	}
}
