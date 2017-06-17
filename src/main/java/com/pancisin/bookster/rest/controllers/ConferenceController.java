package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.events.OnInviteEvent;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Invitation;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.InvitationRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api/conference/{conference_id}")
public class ConferenceController {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'read')")
	public ResponseEntity<?> getConference(@PathVariable Long conference_id) {
		return ResponseEntity.ok(conferenceRepository.findOne(conference_id));
	}

	@PutMapping
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> putConference(@PathVariable Long conference_id, @RequestBody Conference conference) {
		Conference stored = conferenceRepository.findOne(conference_id);
		stored.setName(conference.getName());
		stored.setSummary(conference.getSummary());
		stored.setVisibility(conference.getVisibility());
		return ResponseEntity.ok(conferenceRepository.save(stored));
	}

	@GetMapping("/event/{page}/{size}")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'read')")
	public ResponseEntity<?> getEvents(@PathVariable Long conference_id, @PathVariable int page,
			@PathVariable int size) {
		return ResponseEntity.ok(eventRepository.getByConference(conference_id,
				new PageRequest(page, size, new Sort(Direction.ASC, "date"))));
	}

	@PostMapping("/event")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> postEvent(@PathVariable Long conference_id, @RequestBody Event event) {
		Conference stored = conferenceRepository.findOne(conference_id);
		event.setConference(stored);
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@GetMapping("/attendees")
	@JsonView(Summary.class)
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> getAttendees(@PathVariable Long conference_id) {
//		Conference conference = conferenceRepository.findOne(conference_id);
//		return ResponseEntity.ok(conference.getAttendees());
		return null;
	}

	@PostMapping("/invite")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> postInvitation(@PathVariable Long conference_id, @RequestBody Invitation invitation) {
		Conference conference = conferenceRepository.findOne(conference_id);

		Invitation inv = new Invitation(conference, invitation.getEmail());
		inv.setUser(userRepository.findByEmail(invitation.getEmail()));

		inv = invitationRepository.save(inv);

		if (inv != null)
			eventPublisher.publishEvent(new OnInviteEvent(inv));

		return ResponseEntity.ok(inv);
	}

	@GetMapping("/invitation")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> getInvitations(@PathVariable Long conference_id) {
		Conference conference = conferenceRepository.findOne(conference_id);
		return ResponseEntity.ok(conference.getInvitations());
	}
}
