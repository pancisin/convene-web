package com.pancisin.bookster.rest.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private UserRepository userRepository;

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

	@GetMapping("/user")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> getUsers(@RequestParam int page, @RequestParam int size) {
		return ResponseEntity.ok(userRepository.findAll(new PageRequest(page, size)));
	}
}
