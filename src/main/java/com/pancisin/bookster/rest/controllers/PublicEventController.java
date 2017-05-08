package com.pancisin.bookster.rest.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.EventRepository;

@RestController
@RequestMapping("/public/api/event")
public class PublicEventController {
	
	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping("/public/{page}/{limit}")
	public ResponseEntity<?> getPublic(@PathVariable int page, @PathVariable int limit) {
		return ResponseEntity.ok(eventRepository.getPublic(new PageRequest(page, limit)));
	}
	
	@PreAuthorize("hasPermission(#event_id, 'event', '')")
	@GetMapping("/{event_id}")
	public ResponseEntity<?> getEvent(@PathVariable Long event_id, Principal principal) {
		return ResponseEntity.ok(eventRepository.findOne(event_id));
	}
}
