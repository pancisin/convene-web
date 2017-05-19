package com.pancisin.bookster.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.enums.Unit;

@RestController
@RequestMapping("/api/enum")
public class EnumsController {

	@GetMapping("/unit")
	public ResponseEntity<?> getUnits() {
		return ResponseEntity.ok(Unit.values());
	}
}
