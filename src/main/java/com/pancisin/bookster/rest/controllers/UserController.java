package com.pancisin.bookster.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.components.EmailService;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Locale;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PageAdministratorRepository paRepository;

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

	@GetMapping("/notification")
	public ResponseEntity<?> getNotifications() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(notificationRepository.getUserNotifications(auth.getId()));
	}

	@Autowired
	private EventRepository eventRepository;

	@GetMapping("/event")
	public ResponseEntity<?> getEvents() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(eventRepository.getOwned(auth.getId()));
	}

	@PostMapping("/event")
	@PreAuthorize("hasPermission('event', 'create')")
	public ResponseEntity<?> postEvent(@Valid @RequestBody Event event, BindingResult bindingResult) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		event.setOwner(stored);
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@GetMapping("/event/attending")
	public ResponseEntity<?> getAttendingEvents() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(eventRepository.getAttending(auth.getId()));
	}

	@JsonView(Summary.class)
	@GetMapping("/page")
	public ResponseEntity<?> getPage() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		return ResponseEntity.ok(stored.getPages());
	}

	@PostMapping("/page")
	@PreAuthorize("hasPermission('page', 'create')")
	public ResponseEntity<?> postPage(@RequestBody Page page) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Page stored_page = pageRepository.save(page);
		PageAdministrator pa = new PageAdministrator(page, auth, true);
		pa.setRole(Role.ROLE_OWNER);
		paRepository.save(pa);

		return ResponseEntity.ok(stored_page);
	}

	@GetMapping("/conference")
	public ResponseEntity<?> getConference() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		return ResponseEntity.ok(stored.getConferences());
	}

	@PostMapping("/conference")
	public ResponseEntity<?> postConference(@RequestBody Conference conference) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		conference.setOwner(stored);
		return ResponseEntity.ok(conferenceRepository.save(conference));
	}

	@PutMapping("/locale")
	public ResponseEntity<?> changeLocale(@RequestBody Locale locale) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		stored.setLocale(locale);
		return ResponseEntity.ok(userRepository.save(stored));
	}
}
