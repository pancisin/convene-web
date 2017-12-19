package com.pancisin.bookster.rest.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.enums.Locale;
import com.pancisin.bookster.models.enums.MetaType;
import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.models.enums.Subscription;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.models.enums.WidgetType;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.BranchRepository;
import com.pancisin.bookster.repository.CategoryRepository;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.MediaRepository;
import com.pancisin.bookster.repository.PageRepository;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class PublicRestController {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private MediaRepository mediaRepository;

	@GetMapping("/near-events/{page}/{limit}")
	public ResponseEntity<?> getNearEvents(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "lat") BigDecimal lat, @RequestParam(name = "lng") BigDecimal lng,
			@RequestParam(name = "distance") Double distance) {

		return ResponseEntity.ok(eventRepository.getEventsByDistance(lat, lng, distance,
				new PageRequest(page, limit, new Sort(Direction.ASC, "date"))));
	}

	@GetMapping("/event/{event_id}")
	public ResponseEntity<Event> getEvent(@PathVariable Long event_id) {
		Event event = eventRepository.findOne(event_id);

		if (event == null)
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);

		if (event.getVisibility() == Visibility.PUBLIC && event.getState() == PageState.PUBLISHED)
			return ResponseEntity.ok(event);
		else
			return new ResponseEntity(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/event/{event_id}/related")
	public ResponseEntity<?> getRelatedEvents(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.getRelated(event_id, new PageRequest(0, 100)));
	}

	@GetMapping("/event/{event_id}/gallery")
	public ResponseEntity<?> getEventGallery(@PathVariable Long event_id) {
		return ResponseEntity.ok(mediaRepository.getByEvent(event_id));
	}

	@GetMapping("/popular-pages/{page}/{limit}")
	public ResponseEntity<?> getPopularPages(@PathVariable int page, @PathVariable int limit) {
		return ResponseEntity.ok(pageRepository.getPopular(new PageRequest(page, limit)));
	}

	@GetMapping("/page/{page_id}/service")
	public ResponseEntity<?> getPageServices(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id).getServices());
	}

	@GetMapping("/page/{page_id}/event/{page}/{size}")
	public ResponseEntity<?> getEvents(@PathVariable Long page_id, @PathVariable int page, @PathVariable int size,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		return ResponseEntity.ok(eventRepository.getByPageRange(page_id,
				new PageRequest(page, size, new Sort(Direction.ASC, "date")), fromDate, toDate, null));
	}

	@GetMapping("/user/{user_id}/event")
	public ResponseEntity<?> getUserEvents(@PathVariable Long user_id) {
		return ResponseEntity
				.ok(eventRepository.getByUser(user_id, new PageRequest(0, 100, new Sort(Direction.ASC, "date"))));
	}

	@GetMapping("/categories")
	public ResponseEntity<?> getCategories() {
		return ResponseEntity.ok(categoryRepository.getUsed());
	}

	@GetMapping("/categories/{category_id}/branches")
	public ResponseEntity<?> getBraches(@PathVariable Long category_id) {
		return ResponseEntity.ok(branchRepository.getUsed(category_id));
	}

	@GetMapping("/locales")
	public ResponseEntity<?> getPublic() {
		return ResponseEntity.ok(Locale.values());
	}

	@GetMapping("/subscriptions")
	public ResponseEntity<?> getSubscriptions() {
		return ResponseEntity.ok(Subscription.values());
	}

	@GetMapping("/meta-types")
	public ResponseEntity<?> getMetaTypes() {
		return ResponseEntity.ok(MetaType.values());
	}

	@GetMapping("/widget-types")
	public ResponseEntity<?> getWidgetTypes() {
		return ResponseEntity.ok(WidgetType.values());
	}

	@GetMapping("/conference/{conference_id}")
	public ResponseEntity<Conference> getConference(@PathVariable Long conference_id) {
		Conference conference = conferenceRepository.findOne(conference_id);
		
		if (conference == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
		
		if (conference.getVisibility() == Visibility.PUBLIC && (conference.getState() == PageState.PUBLISHED || conference.getState() == PageState.BLOCKED)) {
			return ResponseEntity.ok(conference);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/conference/{conference_id}/event")
	public ResponseEntity<?> getConferenceEvents(@PathVariable Long conference_id) {
		org.springframework.data.domain.Page<Event> eventsPaginator = eventRepository.getByConference(conference_id,
				new PageRequest(0, 10));
		return ResponseEntity.ok(eventsPaginator.getContent());
	}

	@GetMapping("/conference/{conference_id}/article/{page}/{size}")
	public ResponseEntity<?> getConferenceArticles(@PathVariable Long conference_id, @PathVariable int page,
			@PathVariable int size) {
		return ResponseEntity
				.ok(articleRepository.getByConference(conference_id, new PageRequest(page, size, Direction.DESC, "created")));
	}

	@GetMapping("/conference/{page}/{size}")
	public ResponseEntity<?> getConferences(@PathVariable int page, @PathVariable int size) {
		return ResponseEntity.ok(conferenceRepository.getPublic(new PageRequest(page, size)));
	}

	@ExceptionHandler
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
		System.err.println(e);
	}
}
