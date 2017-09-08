package com.pancisin.bookster.rest.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.components.Notifier;
import com.pancisin.bookster.components.annotations.ActivityLog;
import com.pancisin.bookster.components.annotations.License;
import com.pancisin.bookster.components.annotations.LicenseLimit;
import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.models.BookRequest;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.Widget;
import com.pancisin.bookster.models.enums.ActivityType;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.enums.Subscription;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.ActivityRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.PlaceRepository;
import com.pancisin.bookster.repository.ServiceRepository;
import com.pancisin.bookster.repository.UserRepository;

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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@GetMapping
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getPage(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id));
	}

	@DeleteMapping
	@ActivityLog(type = ActivityType.DELETE)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> deletePage(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		stored.setState(PageState.DELETED);
		return ResponseEntity.ok(pageRepository.save(stored));
	}

	@PutMapping
	@ActivityLog(type = ActivityType.UPDATE)
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

	@GetMapping("/event/{page}/{size}")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getEvents(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size,
			@RequestParam("fromDate") String fromDate) {

		return ResponseEntity.ok(
				eventRepository.getByPageFrom(page_id, new PageRequest(page, size, new Sort(Direction.ASC, "date")), fromDate));
	}

	@PostMapping("/event")
	@LicenseLimit(entity = "event", parent = "page", parentId = "page_id")
	@ActivityLog(type = ActivityType.CREATE_EVENT)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postEvent(@PathVariable("page_id") Long page_id, @RequestBody Event event) {
		Page stored = pageRepository.findOne(page_id);
		event.setPage(stored);
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@PatchMapping("/toggle-follow")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	@ActivityLog(type = ActivityType.FOLLOWING)
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
	@LicenseLimit(entity = "service", parent = "page", parentId = "page_id")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	@ActivityLog(type = ActivityType.CREATE_SERVICE)
	public ResponseEntity<?> postService(@PathVariable("page_id") Long page_id, @RequestBody Service service) {
		Page stored = pageRepository.findOne(page_id);
		service.setPage(stored);
		return ResponseEntity.ok(serviceRepository.save(service));
	}

	@GetMapping("/followers")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getFollowers(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getFollowers());
	}

	@GetMapping("/requests")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getRequests(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);

		List<BookRequest> requests = stored.getServices().stream().flatMap(s -> s.getRequests().stream())
				.collect(Collectors.toList());

		return ResponseEntity.ok(requests);
	}

	@GetMapping("/administrator")
	@JsonView(Summary.class)
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getAdministrators(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getPageAdministrators());
	}

	@PostMapping("/administrator")
	// @License(value = Subscription.PROFESSIONAL, parent = "page", parentId =
	// "page_id")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	@ActivityLog(type = ActivityType.CREATE_ADMINISTRATOR)
	public ResponseEntity<?> postAdministrator(@PathVariable Long page_id, @RequestBody User user) {
		Page stored = pageRepository.findOne(page_id);

		User existing = userRepository.findByEmail(user.getEmail());
		if (existing != null) {
			PageAdministrator pa = new PageAdministrator(stored, existing, false);
			pa.setRole(PageRole.ROLE_ADMINISTRATOR);

			paRepository.save(pa);
			return ResponseEntity.ok(pa);
		}

		return new ResponseEntity("User not found by email", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/place")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getPlaces(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getPlaces());
	}

	@PostMapping("/place")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	@ActivityLog(type = ActivityType.CREATE_PLACE)
	public ResponseEntity<?> postPlace(@PathVariable Long page_id, @RequestBody Place place) {
		Page stored = pageRepository.findOne(page_id);
		place.setPage(stored);
		return ResponseEntity.ok(placeRepository.save(place));
	}

	@PatchMapping("/toggle-published")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	@ActivityLog(type = ActivityType.UPDATE)
	public ResponseEntity<?> togglePublishState(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);

		if (stored.getState() == PageState.DEACTIVATED) {
			stored.setState(PageState.PUBLISHED);
		} else if (stored.getState() == PageState.PUBLISHED) {
			stored.setState(PageState.DEACTIVATED);
		}

		return ResponseEntity.ok(pageRepository.save(stored));
	}

	@GetMapping("/activity")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getActivity(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(activityRepository.getByPage(stored.getId()));
	}

	@GetMapping("/widget")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getWidgets(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getWidgets());
	}

	@Transactional
	@PutMapping("/widget")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postWidgets(@PathVariable Long page_id, @RequestBody List<Widget> widgets) {
		Page stored = pageRepository.findOne(page_id);
		
		stored.setWidgets(widgets);
		pageRepository.save(stored);

		return ResponseEntity.ok(stored.getWidgets());
	}
	
	@ExceptionHandler
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
		System.err.println(e);
	}
}
