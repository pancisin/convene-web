package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.ProgrammeRepository;

@RestController
@PreAuthorize("hasPermission(#event_id, 'event', '')")
@RequestMapping("/api/event/{event_id}")
public class EventController {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ProgrammeRepository programmeRepository;
	
	@GetMapping
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.findOne(event_id));
	}
	
	@PutMapping
	public ResponseEntity<?> putEvent(@PathVariable Long event_id, @RequestBody Event event) {
		Event stored = eventRepository.findOne(event_id);
		
		stored.setName(event.getName());
		stored.setSummary(event.getSummary());
		stored.setVisibility(event.getVisibility());
		stored.setDate(event.getDate());
		
		return ResponseEntity.ok(eventRepository.save(stored));
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteEvent(@PathVariable Long event_id) {
		eventRepository.delete(event_id);
		return ResponseEntity.ok("success");
	}
	
	@PostMapping("/programme")
	public ResponseEntity<?> postProgramme(@PathVariable Long event_id, @RequestBody Programme programme) {
		Event event = eventRepository.findOne(event_id);
		programme.setEvent(event);
		return ResponseEntity.ok(programmeRepository.save(programme));
	}
}
