package com.pancisin.bookster.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.components.annotations.License;
import com.pancisin.bookster.events.OnInviteEvent;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.ConferenceAdministrator;
import com.pancisin.bookster.models.ConferenceAttendee;
import com.pancisin.bookster.models.ConferenceMetaField;
import com.pancisin.bookster.models.ConferenceMetaValue;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Invitation;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.Subscription;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.ConferenceAdministratorRepository;
import com.pancisin.bookster.repository.ConferenceAttendeeRepository;
import com.pancisin.bookster.repository.ConferenceMetaFieldRepository;
import com.pancisin.bookster.repository.ConferenceMetaValueRepository;
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

	@Autowired
	private ConferenceAdministratorRepository caRepository;

	@Autowired
	private ConferenceMetaFieldRepository cmfRepository;

	@Autowired
	private ConferenceMetaValueRepository cmvRepository;

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

	@Autowired
	private ConferenceAttendeeRepository caattendeeRepository;

	@GetMapping("/attendees")
	// @JsonView(Summary.class)
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> getAttendees(@PathVariable Long conference_id) {
		Conference conference = conferenceRepository.findOne(conference_id);
		return ResponseEntity.ok(caattendeeRepository.findByConference(conference_id));
	}

	@PostMapping("/attend")
	@Transactional
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'read')")
	public ResponseEntity<?> postAttend(@PathVariable Long conference_id, @RequestBody List<ConferenceMetaValue> meta) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Conference conference = conferenceRepository.findOne(conference_id);
		ConferenceAttendee attendee = new ConferenceAttendee(user, conference, meta);
		final ConferenceAttendee att = caattendeeRepository.save(attendee);
		
		meta.stream().forEach(x -> x.setAttendee(att));
		cmvRepository.save(meta);
		
		return ResponseEntity.ok(attendee);
	}

	@PutMapping("/cancel-attend")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'read')")
	public ResponseEntity<?> cancelAttend(@PathVariable Long conference_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ConferenceAttendee attendee = caattendeeRepository.findByAttendance(conference_id, user.getId());

		attendee.setActive(false);
		return ResponseEntity.ok(caattendeeRepository.save(attendee));
	}

	@GetMapping("/attend-status")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'read')")
	public ResponseEntity<?> getAttendStatus(@PathVariable Long conference_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ConferenceAttendee attendee = caattendeeRepository.findByAttendance(conference_id, user.getId());

		if (attendee == null)
			return ResponseEntity.ok("INACTIVE");

		return ResponseEntity.ok(attendee.isActive() ? "ACTIVE" : "CANCELED");
	}

	@GetMapping("/meta-field")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> getMetaFields(@PathVariable Long conference_id) {
		return ResponseEntity.ok(cmfRepository.findByConferenceId(conference_id));
	}

	@PostMapping("/meta-field")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> postMetaField(@PathVariable Long conference_id, @RequestBody ConferenceMetaField field) {
		Conference conference = conferenceRepository.findOne(conference_id);
		field.setConference(conference);
		return ResponseEntity.ok(cmfRepository.save(field));
	}

	@PostMapping("/invitation")
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

	@GetMapping("/administrator")
	@JsonView(Summary.class)
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> getAdministrators(@PathVariable Long conference_id) {
		Conference stored = conferenceRepository.findOne(conference_id);
		return ResponseEntity.ok(stored.getConferenceAdministrators());
	}

	@PostMapping("/administrator")
	// @License(value = Subscription.ENTERPRISE, parent = "conference", parentId
	// = "conference_id")
	@PreAuthorize("hasPermission(#conference_id, 'conference', 'update')")
	public ResponseEntity<?> postAdministrator(@PathVariable Long conference_id, @RequestBody User user) {
		Conference stored = conferenceRepository.findOne(conference_id);

		ConferenceAdministrator pa = new ConferenceAdministrator(stored, user, false);
		pa.setRole(PageRole.ROLE_ADMINISTRATOR);

		caRepository.save(pa);
		return ResponseEntity.ok(pa);
	}
}
