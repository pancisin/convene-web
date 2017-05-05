package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Event;
import com.pancisin.employger.models.Programme;
import com.pancisin.employger.repository.EventRepository;
import com.pancisin.employger.repository.ProgrammeRepository;

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
