package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.ConferenceRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@GetMapping("/events/{page}/{size}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return null;
	}

	@GetMapping("/conferences/{page}/{size}")
	public ResponseEntity<?> getConferences(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(conferenceRepository.getForUser(auth.getId(), new PageRequest(page, size)));
	}
}
