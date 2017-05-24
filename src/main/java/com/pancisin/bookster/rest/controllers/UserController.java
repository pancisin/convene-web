package com.pancisin.bookster.rest.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.components.EmailService;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Locale;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.PlaceRepository;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.repository.UserSearchRepository;
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

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private UserSearchRepository userSearchRepository;

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

	@GetMapping("/event/{page}/{size}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(eventRepository.getOwned(auth.getId(), new PageRequest(page, size)));
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

	@GetMapping("/subscription")
	public ResponseEntity<?> getSubscriptions() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		return ResponseEntity.ok(stored.getSubscriptions());
	}

	@PostMapping("/place")
	public ResponseEntity<?> postPlace(@RequestBody Place place) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		place.setUser(auth);
		return ResponseEntity.ok(placeRepository.save(place));
	}

	@GetMapping("/place")
	public ResponseEntity<?> getPlace() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());
		return ResponseEntity.ok(stored.getPlaces());
	}

	@JsonView(Summary.class)
	@GetMapping("/search")
	public ResponseEntity<?> searchUser(@RequestParam String q) {
		List result = null;

		try {
			result = userSearchRepository.search(q);
		} catch (Exception ex) {
			System.err.println(ex);
		}

		return ResponseEntity.ok(result);
	}

	@ExceptionHandler
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
		System.err.println(e);
	}
}
