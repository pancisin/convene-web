package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.repository.EventRepository;

@RestController
@PreAuthorize("hasPermission(#event_id, 'event', '')")
@RequestMapping("/api/event/{event_id}")
public class EventController {

	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.findOne(event_id));
	}
}
