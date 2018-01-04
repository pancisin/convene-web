package com.pancisin.bookster.rest.controllers;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.pancisin.bookster.model.Administrator;
import com.pancisin.bookster.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
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

import com.pancisin.bookster.components.annotations.LicenseLimit;
import com.pancisin.bookster.components.storage.StorageService;
import com.pancisin.bookster.model.Conference;
import com.pancisin.bookster.model.Event;
import com.pancisin.bookster.model.Page;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.model.UserSubscription;
import com.pancisin.bookster.model.enums.Locale;
import com.pancisin.bookster.model.enums.PageRole;
import com.pancisin.bookster.model.enums.Role;
import com.pancisin.bookster.model.enums.Subscription;
import com.pancisin.bookster.model.enums.SubscriptionState;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.MediaRepository;
import com.pancisin.bookster.repository.NotificationRepository;
import com.pancisin.bookster.repository.AdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;
import com.pancisin.bookster.repository.UserSearchRepository;
import com.pancisin.bookster.repository.UserSubscriptionRepository;
import com.pancisin.bookster.rest.controllers.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/user")
public class CurrentUserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private UserSearchRepository userSearchRepository;

	@Autowired
	private UserSubscriptionRepository usRepository;

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private StorageService storageService;

	@GetMapping("/me")
	public ResponseEntity<User> getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		User stored = userRepository.findOne(auth_user.getId());
		return ResponseEntity.ok(stored);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<User> updateMe(@RequestBody User user) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		stored.setFirstName(user.getFirstName());
		stored.setLastName(user.getLastName());
		stored.setAddress(user.getAddress());
		stored.setMetadata(user.getMetadata());

		if (user.getProfilePictureData() != null && storageService.isBinary(user.getProfilePictureData())) {
			Media profilePicture = new Media();
			// profilePicture.setAuthor(stored);
			profilePicture = mediaRepository.save(profilePicture);
			String url = "images/users/" + profilePicture.getId().toString();

			profilePicture.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(user.getProfilePictureData(), url);
			profilePicture.setSize(size);
			stored.setProfilePicture(profilePicture);
		}

		userRepository.save(stored);
		return ResponseEntity.ok(stored);
	}

	@GetMapping("/notification/{page}/{size}")
	public ResponseEntity<?> getNotifications(@PathVariable int page, @PathVariable int size,
			@RequestParam(name = "seen", defaultValue = "false") boolean seen) {

		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(notificationRepository.findByRecipientId(auth.getId(), seen,
				new PageRequest(page, size, new Sort(Direction.DESC, "created"))));
	}

	@Autowired
	private EventRepository eventRepository;

	@GetMapping("/event/{page}/{size}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int size,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok(eventRepository.getOwned(auth.getId(), new PageRequest(page, size), fromDate, toDate));
	}

	@PostMapping("/event")
	@LicenseLimit(entity = "event")
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

	@GetMapping("/page")
	public ResponseEntity<?> getPage() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(pageRepository.getByOwner(auth.getId()));
	}

	@PostMapping("/page")
	@LicenseLimit(entity = "page")
	public ResponseEntity<?> postPage(@RequestBody Page page) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Page stored_page = pageRepository.save(page);
		Administrator pa = new Administrator(page, auth, true);
		pa.setRole(PageRole.ROLE_OWNER);
		administratorRepository.save(pa);

		return ResponseEntity.ok(stored_page);
	}

	@GetMapping("/conference")
	public ResponseEntity<?> getConference() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(conferenceRepository.getByOwner(auth.getId()));
	}

	@PostMapping("/conference")
	@LicenseLimit(entity = "conference")
	@Transactional
	public ResponseEntity<?> postConference(@RequestBody Conference conference) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Conference stored_conference = conferenceRepository.save(conference);

		Administrator ca = new Administrator(stored_conference, auth, true);
		ca.setRole(PageRole.ROLE_OWNER);
		administratorRepository.save(ca);

		return ResponseEntity.ok(stored_conference);
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

	@Transactional
	@PostMapping("/subscription")
	public ResponseEntity<?> postSubscription(@RequestBody UserSubscription su) throws Exception {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		if (stored.getLicense() == null || stored.getLicense().getSubscription() == Subscription.FREE) {
			if (!stored.isVerified())
				throw new Exception("User email is not verified !");

			if (stored.getRole() != Role.ROLE_AUTHOR) {
				stored.setRole(Role.ROLE_AUTHOR);
			}

			stored.setAddress(su.getUser().getAddress());
			userRepository.save(stored);

			su.setState(SubscriptionState.ACTIVE);
			su.setUser(stored);

			Calendar expiration = Calendar.getInstance();
			expiration.add(Calendar.MONTH, 1);

			su.setExpires(expiration);

			return ResponseEntity.ok(usRepository.save(su));
		} else
			return null;
	}

	// @PostMapping("/place")
	// public ResponseEntity<?> postPlace(@RequestBody Place place) {
	// User auth = (User)
	// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	// place.setUser(auth);
	// return ResponseEntity.ok(placeRepository.save(place));
	// }

	// @GetMapping("/place")
	// public ResponseEntity<?> getPlace() {
	// User auth = (User)
	// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	// User stored = userRepository.findOne(auth.getId());
	// return ResponseEntity.ok(stored.getPlaces());
	// }

	@GetMapping("/search")
	public ResponseEntity<?> searchUser(@RequestParam String q) {
		List<User> result = null;

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

	@GetMapping("/contacts")
	public ResponseEntity<?> getContacts() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(administratorRepository.getContacts(auth.getId()));
	}

	@GetMapping("/followed-pages")
	public ResponseEntity<?> getFollowedPages() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(pageRepository.getFollowed(auth.getId()));
	}

	@GetMapping("/media")
	public ResponseEntity<?> getMedias() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(mediaRepository.getByAuthor(auth.getId()));
	}

	@Transactional
	@PatchMapping("/set-notifications-seen")
	public ResponseEntity<?> setNotificationsSeen(@RequestParam("since") Long sinceTimestamp,
			@RequestParam("until") Long untilTimestamp, @RequestParam("seen") boolean seen) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Calendar sinceCal = Calendar.getInstance();
		sinceCal.setTimeInMillis(sinceTimestamp);

		Calendar untilCal = Calendar.getInstance();
		untilCal.setTimeInMillis(untilTimestamp);

		return ResponseEntity.ok(notificationRepository.setSeen(auth.getId(), sinceCal, untilCal, seen));
	}
}
