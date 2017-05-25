package com.pancisin.bookster.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Category;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.BranchRepository;
import com.pancisin.bookster.repository.CategoryRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.LocaleRepository;
import com.pancisin.bookster.repository.PageRepository;

@RestController
@RequestMapping("/public")
public class PublicRestController {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private LocaleRepository localeRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BranchRepository branchRepository;

	@GetMapping("/events/{page}/{limit}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int limit) {
		return ResponseEntity.ok(eventRepository.getPublic(new PageRequest(page, limit)));
	}

	@GetMapping("/event/{event_id}")
	public ResponseEntity<?> getEvent(@PathVariable Long event_id) {
		Event event = eventRepository.findOne(event_id);

		if (event.getVisibility() == Visibility.PUBLIC)
			return ResponseEntity.ok(event);
		else
			return null;
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
			pages = pageRepository.findAll(pageable);
		}

		return ResponseEntity.ok(pages);
	}

	@GetMapping("/page/{page_id}")
	public ResponseEntity<?> getPage(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id));
	}

	@GetMapping("/page/{page_id}/service")
	public ResponseEntity<?> getPageServices(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id).getServices());
	}

	@GetMapping("/page/{page_id}/event")
	public ResponseEntity<?> getPageEvents(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id).getEvents());
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
		return ResponseEntity.ok(localeRepository.findAll());
	}

	@ExceptionHandler
	@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
		System.err.println(e);
	}
}
