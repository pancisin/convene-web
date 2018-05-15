package com.pancisin.bookster.rest.controllers;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import com.pancisin.bookster.model.*;
import com.pancisin.bookster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.annotations.ActivityLog;
import com.pancisin.bookster.components.annotations.LicenseLimit;
import com.pancisin.bookster.components.storage.StorageServiceImpl;
import com.pancisin.bookster.model.enums.ActivityType;
import com.pancisin.bookster.model.enums.PageRole;
import com.pancisin.bookster.model.enums.PageState;

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
	private AdministratorRepository paRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private EventBotRepository eventBotRepository;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
  private PageMemberRepository pageMemberRepository;

	@Autowired
  private FormSubmissionRepository formSubmissionRepository;

	// @GetMapping
	// @PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	// public ResponseEntity<?> getPage(@PathVariable Long page_id) {
	// Page page = pageRepository.findOne(page_id);
	//
	// if (page == null)
	// return new ResponseEntity(HttpStatus.NOT_FOUND);
	// else
	// return ResponseEntity.ok(page);
	// }

	@DeleteMapping
	@ActivityLog(type = ActivityType.DELETE)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> deletePage(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		stored.setState(PageState.DELETED);
		stored.setFacebookId(null);
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
		stored.setMetadata(page.getMetadata());

		if (page.getPosterData() != null && storageService.isBinary(page.getPosterData())) {
			Media poster = new Media();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			poster.setAuthor(user);
			poster = mediaRepository.save(poster);
			String url = "banners/pages/" + poster.getId().toString();

			poster.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(page.getPosterData(), url);
			poster.setSize(size);
			stored.setPoster(poster);
		}

		return ResponseEntity.ok(pageRepository.save(stored));
	}

	@GetMapping("/event/{page}/{size}")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getEvents(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok(eventRepository.getByPageRange(page_id,
				new PageRequest(page, size, new Sort(Direction.ASC, "date")), fromDate, toDate, user.getId()));
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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    PageMember member = pageMemberRepository.findByAttendance(page_id, user.getId());

    if (member != null) {
      member.setActive(!member.getActive());
      pageMemberRepository.save(member);
    } else {
		  Page stored = pageRepository.findOne(page_id);
		  member = new PageMember(user, stored);
      pageMemberRepository.save(member);
    }

		return ResponseEntity.ok(member);
	}

	@GetMapping("/follow-status")
	@PreAuthorize("hasPermission(#page_id, 'page', 'read')")
	public ResponseEntity<?> getFollowStatus(@PathVariable Long page_id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	  return ResponseEntity.ok(pageMemberRepository.findByAttendance(page_id, user.getId()) != null);
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
		return ResponseEntity.ok(stored.getMembers());
	}

	@GetMapping("/requests/{page}/{size}")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getRequests(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size) {
//		org.springframework.data.domain.Page<ServiceRequest> requests = serviceRequestRepository.getByPage(page_id, new PageRequest(page, size));
//		return ResponseEntity.ok(requests);

		return ResponseEntity.ok(formSubmissionRepository.getServiceRequestsByPage(page_id));
	}

	@GetMapping("/administrator")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getAdministrators(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getAdministrators());
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

			if (stored.getAdministrators().stream().anyMatch(a -> a.getUser().getId() == existing.getId())) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}

			Administrator pa = new Administrator(stored, existing, false);
			pa.setRole(PageRole.ROLE_ADMINISTRATOR);

			paRepository.save(pa);
			return ResponseEntity.ok(pa);
		}

		return new ResponseEntity<String>("User not found by email", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/place")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getPlaces(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getPlaces());
	}

	@Transactional
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

	@GetMapping("/activity/{page}/{size}")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getActivity(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity
				.ok(activityRepository.getByPage(stored.getId(), new PageRequest(page, size, Direction.DESC, "created")));
	}

	@GetMapping("/widget")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getWidgets(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		return ResponseEntity.ok(stored.getWidgets());
	}

	@Transactional
	@PutMapping("/widget")
	@ActivityLog(type = ActivityType.UPDATE)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postWidgets(@PathVariable Long page_id, @RequestBody List<Widget> widgets) {
		Page stored = pageRepository.findOne(page_id);

		stored.setWidgets(widgets);
		pageRepository.save(stored);

		return ResponseEntity.ok(stored.getWidgets());
	}

	@GetMapping("/gallery")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read')")
	public ResponseEntity<?> getGallery(@PathVariable Long page_id) {
		return ResponseEntity.ok(mediaRepository.getByPage(page_id));
	}

	@PostMapping("/gallery")
	@ActivityLog(type = ActivityType.POST_MEDIA)
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> postGallery(@PathVariable Long page_id, @RequestBody Media galleryItem) {
		Page stored = pageRepository.findOne(page_id);

		if (storageService.isBinary(galleryItem.getData())) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			galleryItem.setAuthor(user);

			galleryItem = mediaRepository.save(galleryItem);
			String url = "images/page/" + galleryItem.getId().toString();

			galleryItem.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(galleryItem.getData(), url);
			galleryItem.setSize(size);
			stored.addGallery(galleryItem);
		}

		pageRepository.save(stored);

		return ResponseEntity.ok(galleryItem);
	}

	@GetMapping("/bot/{page}/{size}")
	@PreAuthorize("hasPermission(#page_id, 'page', 'admin-read') AND hasRole('SUPERADMIN')")
	public ResponseEntity<?> getEventBots(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size) {
		return ResponseEntity.ok(eventBotRepository.getByPage(page_id, new PageRequest(page, size)));
	}

	@PostMapping("/bot")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update') AND hasRole('SUPERADMIN')")
	public ResponseEntity<?> postEventBot(@PathVariable Long page_id, @RequestBody EventBot eventBot) {
		Page stored = pageRepository.findOne(page_id);
		eventBot.setPage(stored);
		return ResponseEntity.ok(eventBotRepository.save(eventBot));
	}

	@PutMapping("/formField")
	@PreAuthorize("hasPermission(#page_id, 'page', 'update')")
	public ResponseEntity<?> putMetaData(@PathVariable Long page_id, HashMap<String, String> metadata) {
		Page stored = pageRepository.findOne(page_id);

		stored.setMetadata(metadata);
		pageRepository.save(stored);
		return ResponseEntity.ok(metadata);
	}
}
