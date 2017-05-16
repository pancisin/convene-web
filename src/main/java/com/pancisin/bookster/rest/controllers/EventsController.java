package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
public class EventsController {

	@Autowired
	private EventRepository eventRepository;

	@GetMapping("/{page}/{limit}")
	public ResponseEntity<?> getPublic(@PathVariable int page, @PathVariable int limit) {
		return ResponseEntity
				.ok(eventRepository.getPublic(new PageRequest(page, limit, new Sort(Sort.Direction.DESC, "date"))));
	}
}
