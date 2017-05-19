package com.pancisin.bookster.rest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.pancisin.bookster.components.Notifier;
import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.PlaceRepository;
import com.pancisin.bookster.repository.ServiceRepository;

@RestController
@RequestMapping("/api/page/{page_id}")
public class PageController {

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private StorageServiceImpl storageService;

	@Autowired
	private Notifier notifier;

	@Autowired
	private PageAdministratorRepository paRepository;

	@Autowired
	private PlaceRepository placeRepository;
	
	@GetMapping
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getPage(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id));
	}

	@DeleteMapping
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> deletePage(@PathVariable Long page_id) {
		pageRepository.delete(page_id);
		return ResponseEntity.ok("success");
	}

	@PutMapping
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> putPage(@PathVariable Long page_id, @RequestBody Page page) {
		Page stored = pageRepository.findOne(page_id);
		stored.setName(page.getName());
		stored.setBranch(page.getBranch());
		stored.setSummary(page.getSummary());

		if (page.getBannerUrl() != null && storageService.isBinary(page.getBannerUrl())) {
			String url = "banners/pages/" + stored.getId();
			storageService.storeBinary(page.getBannerUrl(), url);
			stored.setBannerUrl("/files/" + url + ".jpg");
		}

		return ResponseEntity.ok(pageRepository.save(stored));
	}

	@GetMapping("/event")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getEvents(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getEvents());
	}

	@PostMapping("/event")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postEvent(@PathVariable Long page_id, @RequestBody Event event) {
		Page stored = pageRepository.findOne(page_id);
		event.setPage(stored);
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@PatchMapping("/toggle-follow")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> followPage(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		boolean status = stored.getFollowers().stream().anyMatch(x -> x.getId() == user.getId());
		if (status)
			stored.setFollowers(
					stored.getFollowers().stream().filter(x -> x.getId() != user.getId()).collect(Collectors.toList()));
		else {
			stored.getFollowers().add(user);
			stored.getPageAdministrators().stream().forEach(x -> notifier.notifyUser(x.getUser(), "New follower !",
					user.getEmail() + " has started to following your page."));
		}

		pageRepository.save(stored);
		return ResponseEntity.ok(!status);
	}

	@GetMapping("/follow-status")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getFollowStatus(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(stored.getFollowers().stream().anyMatch(x -> x.getId() == user.getId()));
	}

	@GetMapping("/service")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getServices(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getServices());
	}

	@PostMapping("/service")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postService(@PathVariable Long page_id, @RequestBody Service service) {
		Page stored = pageRepository.findOne(page_id);
		service.setPage(stored);
		return ResponseEntity.ok(serviceRepository.save(service));
	}

	@GetMapping("/followers")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> getFollowers(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getFollowers());
	}

	@GetMapping("/requests")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> getRequests(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);

		List<BookRequest> requests = stored.getServices().stream().flatMap(s -> s.getRequests().stream())
				.collect(Collectors.toList());

		return ResponseEntity.ok(requests);
	}

	@GetMapping("/administrator")
	@JsonView(Summary.class)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> getAdministrators(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getPageAdministrators());
	}

	@PostMapping("/administrator")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postAdministrator(@PathVariable Long page_id, @RequestBody User user) {
		Page stored = pageRepository.findOne(page_id);

		PageAdministrator pa = new PageAdministrator(stored, user, false);
		pa.setRole(Role.ROLE_ADMINISTRATOR);

		return ResponseEntity.ok(paRepository.save(pa));
	}

	@GetMapping("/place")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> getPlaces(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getPlaces());
	}

	@PostMapping("/place")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postPlace(@PathVariable Long page_id, @RequestBody Place place) {
		Page stored = pageRepository.findOne(page_id);

		if (stored.getPlaces() == null) 
			stored.setPlaces(new ArrayList<Place>());
		
		placeRepository.save(place);
		stored.getPlaces().add(place);
		pageRepository.save(stored);
		
		return ResponseEntity.ok(place);
	}
}
