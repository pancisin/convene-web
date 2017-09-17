package com.pancisin.bookster.rest.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.events.OnInviteEvent;
import com.pancisin.bookster.events.OnRegistrationCompleteEvent;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Invitation;
import com.pancisin.bookster.models.Media;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.InvitationRepository;
import com.pancisin.bookster.repository.MediaRepository;
import com.pancisin.bookster.repository.ProgrammeRepository;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/event/{event_id}")
public class EventController {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private ProgrammeRepository programmeRepository;

	@Autowired
	private StorageServiceImpl storageService;

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private MediaRepository mediaRepository;
	
	@GetMapping
	@PreAuthorize("hasPermission(#event_id, 'event', 'read')")
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.findOne(event_id));
	}

	@PutMapping
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> putEvent(@PathVariable Long event_id, @Valid @RequestBody Event event,
			BindingResult bindingResult) {
		Event stored = eventRepository.findOne(event_id);

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		stored.setName(event.getName());
		stored.setSummary(event.getSummary());
		stored.setVisibility(event.getVisibility());
		stored.setDate(event.getDate());
		stored.setPlace(event.getPlace());

		if (event.getPosterData() != null && storageService.isBinary(event.getPosterData())) {
			Media poster = new Media();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			poster.setAuthor(user);
			poster = mediaRepository.save(poster);
			String url = "banners/events/" + poster.getId().toString();
			
			poster.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(event.getPosterData(), url);
			poster.setSize(size);
			stored.setPoster(poster);
		}

		return ResponseEntity.ok(eventRepository.save(stored));
	}

	@DeleteMapping
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> deleteEvent(@PathVariable Long event_id) {
		eventRepository.delete(event_id);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/programme")
	public ResponseEntity<?> getProgramme(@PathVariable Long event_id) {
		Event event = eventRepository.findOne(event_id);
		return ResponseEntity.ok(event.getProgramme());
	}
	
	@PostMapping("/programme")
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> postProgramme(@PathVariable Long event_id, @RequestBody Programme programme) {
		Event event = eventRepository.findOne(event_id);
		programme.setEvent(event);
		return ResponseEntity.ok(programmeRepository.save(programme));
	}

	@PatchMapping("/toggle-attend")
	@PreAuthorize("hasPermission(#event_id, 'event', 'read')")
	public ResponseEntity<?> toggleAttend(@PathVariable Long event_id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		int attend_count = eventRepository.isAttending(event_id, auth_user.getId());
		boolean status = false;

		Event event = eventRepository.findOne(event_id);
		if (attend_count > 0) {
			event.setAttendees(event.getAttendees().stream().filter(u -> u.getId() != auth_user.getId())
					.collect(Collectors.toList()));
			status = false;
		} else {
			event.getAttendees().add(auth_user);
			status = true;
		}

		eventRepository.save(event);
		return ResponseEntity.ok(status);
	}

	@GetMapping("/attend-status")
	@PreAuthorize("hasPermission(#event_id, 'event', 'read')")
	public ResponseEntity<?> getAttendStatus(@PathVariable Long event_id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		int attend_count = eventRepository.isAttending(event_id, auth_user.getId());
		return ResponseEntity.ok(attend_count > 0);
	}

	@GetMapping("/attendees")
	@JsonView(Summary.class)
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> getAttendees(@PathVariable Long event_id) {
		Event stored = eventRepository.findOne(event_id);
		return ResponseEntity.ok(stored.getAttendees());
	}

	@PostMapping("/invite")
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> postInvitation(@PathVariable Long event_id, @RequestBody Invitation invitation) {
		Event stored = eventRepository.findOne(event_id);
		Invitation inv = new Invitation(stored, invitation.getEmail());
		inv.setUser(userRepository.findByEmail(invitation.getEmail()));

		inv = invitationRepository.save(inv);

		if (inv != null)
			eventPublisher.publishEvent(new OnInviteEvent(inv));

		return ResponseEntity.ok(inv);
	}

	@GetMapping("/invitation")
	@PreAuthorize("hasPermission(#event_id, 'event', 'update')")
	public ResponseEntity<?> getInvitations(@PathVariable Long event_id) {
		Event stored = eventRepository.findOne(event_id);
		return ResponseEntity.ok(stored.getInvitations());
	}

	@GetMapping("/related")
	public ResponseEntity<?> getRelatedEvents(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.getRelated(event_id, new PageRequest(0, 100)));
	}
	
	@GetMapping("/gallery")
	public ResponseEntity<?> getGallery(@PathVariable Long event_id) {
		return ResponseEntity.ok(mediaRepository.getByEvent(event_id));
	}
	
	@PostMapping("/gallery") 
	public ResponseEntity<?> postGallery(@PathVariable Long event_id, @RequestBody Media galleryItem) {
		Event stored = eventRepository.findOne(event_id);

		if (storageService.isBinary(galleryItem.getData())) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			galleryItem.setAuthor(user);
			
			galleryItem = mediaRepository.save(galleryItem);
			String url = "images/event/" + galleryItem.getId().toString();
			
			galleryItem.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(galleryItem.getData(), url);
			galleryItem.setSize(size);
			stored.AddGallery(galleryItem);
		}
		
		eventRepository.save(stored);
		
		return ResponseEntity.ok(galleryItem);
	}
}
