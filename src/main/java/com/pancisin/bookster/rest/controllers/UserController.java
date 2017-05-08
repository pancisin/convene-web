package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PageRepository pageRepository;

	@GetMapping("/me")
	public ResponseEntity<User> getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		User stored = userRepository.findOne(auth_user.getId());
		return ResponseEntity.ok(stored);
	}

	@PutMapping("/me")
	public ResponseEntity<User> updateMe(@RequestBody User user) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		stored.setFirstName(user.getFirstName());
		stored.setLastName(user.getLastName());

		userRepository.save(stored);
		return ResponseEntity.ok(stored);
	}

	@GetMapping("/me/notifications")
	public ResponseEntity<?> getNotifications() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(notificationRepository.getUserNotifications(auth.getId()));
	}

	@Autowired
	private EventRepository eventRepository;

	@PostMapping("/event")
	public ResponseEntity<?> postEvent(@RequestBody Event event) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		event.setOwner(stored);
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@GetMapping("/event")
	public ResponseEntity<?> getEvent() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		return ResponseEntity.ok(stored.getEvents());
	}

	@PostMapping("/page")
	public ResponseEntity<?> postPage(@RequestBody Page page) {
		return ResponseEntity.ok(pageRepository.save(page));
	}
}
