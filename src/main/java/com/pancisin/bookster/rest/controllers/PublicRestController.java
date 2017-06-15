package com.pancisin.bookster.rest.controllers;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.EventBotService;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.enums.Locale;
import com.pancisin.bookster.models.enums.Subscription;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.BranchRepository;
import com.pancisin.bookster.repository.CategoryRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageRepository;

import facebook4j.FacebookException;

@RestController
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

	@GetMapping("/events/{page}/{limit}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "timestamp", required = false) String timestamp) {

		Date date = null;
		try {
			date = new Date(Long.parseLong(timestamp));
		} catch (NumberFormatException ex) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return ResponseEntity.ok(
				eventRepository.getPublicByDate(cal, new PageRequest(page, limit, new Sort(Direction.ASC, "date"))));
	}

	@GetMapping("/near-events/{page}/{limit}")
	public ResponseEntity<?> getNearEvents(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "lat") BigDecimal lat, @RequestParam(name = "lng") BigDecimal lng,
			@RequestParam(name = "distance") Double distance) {

		return ResponseEntity.ok(eventRepository.getEventsByDistance(lat, lng, distance,
				new PageRequest(page, limit, new Sort(Direction.ASC, "date"))));
	}

	@GetMapping("/event/{event_id}")
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		Event event = eventRepository.findOne(event_id);

		if (event.getVisibility() == Visibility.PUBLIC)
			return ResponseEntity.ok(event);
		else
			return null;
	}
	
	@GetMapping("/event/{event_id}/related")
	public ResponseEntity<?> getRelatedEvents(@PathVariable Long event_id) {
		return ResponseEntity.ok(eventRepository.getRelated(event_id, new PageRequest(0, 100)));
	}

	@GetMapping("/pages/{page}/{limit}")
	public ResponseEntity<?> getPages(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "categoryId", required = false) Long categoryId,
			@RequestParam(name = "branchId", required = false) Long branchId) {

		org.springframework.data.domain.Page<Page> pages = null;
		Pageable pageable = new PageRequest(page, limit, new Sort(Sort.Direction.ASC, "name"));

		if (branchId != null) {
			pages = pageRepository.findByBranch(branchId, pageable);
		} else if (categoryId != null) {
			pages = pageRepository.findByCategory(categoryId, pageable);
		} else {
			pages = pageRepository.findAllVisible(pageable);
		}

		return ResponseEntity.ok(pages);
	}

	@GetMapping("/popular-pages/{page}/{limit}")
	public ResponseEntity<?> getPopularPages(@PathVariable int page, @PathVariable int limit) {
		return ResponseEntity.ok(pageRepository.getPopular(new PageRequest(page, limit)));
	}

	@GetMapping("/page/{page_identifier}")
	public ResponseEntity<?> getPage(@PathVariable Object page_identifier) {
		try {
			Long page_id = Long.parseLong((String) page_identifier);
			return ResponseEntity.ok(pageRepository.findOne(page_id));
		} catch (NumberFormatException ex) {
			return ResponseEntity.ok(pageRepository.findBySlug((String) page_identifier));
		}
	}

	@GetMapping("/page/{page_id}/service")
	public ResponseEntity<?> getPageServices(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id).getServices());
	}

	@GetMapping("/page/{page_id}/event")
	public ResponseEntity<?> getPageEvents(@PathVariable Long page_id) {
//		return ResponseEntity.ok(pageRepository.findOne(page_id).getEvents());
		return ResponseEntity.ok(eventRepository.getByPage(page_id, new PageRequest(0, 100, new Sort(Direction.ASC, "date"))));
	}

	@GetMapping("/user/{user_id}/event")
	public ResponseEntity<?> getUserEvents(@PathVariable Long user_id) {
		return ResponseEntity.ok(eventRepository.getByUser(user_id, new PageRequest(0, 100, new Sort(Direction.ASC, "date"))));
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

	@Autowired
	private EventBotService eventBot;

	@ExceptionHandler
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
		System.err.println(e);
	}
}
