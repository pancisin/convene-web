package com.pancisin.bookster.rest.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Programme;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.ProgrammeRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;

@RestController
@PreAuthorize("hasPermission(#event_id, 'event', '')")
@RequestMapping("/api/event/{event_id}")
public class EventController {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ProgrammeRepository programmeRepository;

	@Autowired
	private StorageServiceImpl storageService;
	
	@GetMapping
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.findOne(event_id));
	}
	
	@PutMapping
	public ResponseEntity<?> putEvent(@PathVariable Long event_id, @Valid @RequestBody Event event, BindingResult bindingResult) {
		Event stored = eventRepository.findOne(event_id);
		
		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);
		
		stored.setName(event.getName());
		stored.setSummary(event.getSummary());
		stored.setVisibility(event.getVisibility());
		stored.setDate(event.getDate());
		
		if (event.getBannerUrl() != null && storageService.isBinary(event.getBannerUrl())) {
			String url = "banners/events/" + stored.getId();
			storageService.storeBinary(event.getBannerUrl(), url);
			stored.setBannerUrl("/files/" + url + ".jpg");
		}
		
		return ResponseEntity.ok(eventRepository.save(stored));
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
	
	@PatchMapping("/toggle-attend")
	public ResponseEntity<?> toggleAttend(@PathVariable Long event_id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		int attend_count = eventRepository.isAttending(event_id, auth_user.getId());
		boolean status = false;
		
		Event event = eventRepository.findOne(event_id);
		if (attend_count > 0) {
			event.setAttendees(event.getAttendees().stream().filter(u -> u.getId() != auth_user.getId()).collect(Collectors.toList()));
			status = false;
		} else {
			event.getAttendees().add(auth_user);
			status = true;
		}
		
		eventRepository.save(event);
		return ResponseEntity.ok(status);
	}
	
	@GetMapping("/attend-status")
	public ResponseEntity<?> getAttendStatus(@PathVariable Long event_id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		int attend_count = eventRepository.isAttending(event_id, auth_user.getId());
		return ResponseEntity.ok(attend_count > 0);
	}
}
