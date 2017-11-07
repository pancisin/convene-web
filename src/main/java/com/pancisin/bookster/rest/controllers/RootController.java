package com.pancisin.bookster.rest.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.ArticlesList;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.ArticlesListRepository;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageRepository;

@RestController
public class RootController {

	@Autowired
	private ArticlesListRepository alRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ConferenceRepository conferenceRepository;

	@GetMapping({ "/api/articles", "/public/articles" })
	public ResponseEntity<?> getArticles(
			@RequestParam(required = false, name = "tags", defaultValue = "language:en,headlines") List<String> tags,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size) {
		int tagsHash = 0;

		if (tags != null && !tags.isEmpty()) {
			List<String> sorted = tags.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
			tagsHash = sorted.hashCode();
		}

		ArticlesList articlesList = alRepository.findByTagsHash(tagsHash);

		if (articlesList != null) {
			Page<Article> articles = articleRepository.getByArticlesList(articlesList.getId(),
					new PageRequest(page, size, Direction.DESC, "created"));
			return ResponseEntity.ok(articles);
		}

		return null;
	}

	@GetMapping({ "/api/pages/{page}/{limit}", "/public/pages/{page}/{limit}" })
	public ResponseEntity<?> getPages(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "categoryId", required = false) Long categoryId,
			@RequestParam(name = "branchId", required = false) Long branchId) {

		org.springframework.data.domain.Page<com.pancisin.bookster.models.Page> pages = null;
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

	@GetMapping({ "/api/events/{page}/{limit}", "/public/events/{page}/{limit}" })
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int limit,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "authorType", required = false, defaultValue = "") String authorType,
			@RequestParam(name = "authorId", required = false, defaultValue = "0") String authorId) {

		Date date = null;
		try {
			date = new Date(Long.parseLong(timestamp));
		} catch (NumberFormatException ex) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		switch (authorType) {
		case "PAGE":
			// TODO
			return null;
		case "USER":
			Long userId = Long.parseLong(authorId);
			return ResponseEntity.ok(eventRepository.getPublicByUser(userId, cal, new PageRequest(page, limit)));
		case "CONFERENCE":
			// TODO
			return null;
		default:
			return ResponseEntity
					.ok(eventRepository.getPublicByDate(cal, new PageRequest(page, limit, new Sort(Direction.ASC, "date"))));
		}
	}

	@GetMapping({ "/api/conferences/{page}/{size}", "/public/conferences/{page}/{size}" })
	public ResponseEntity<?> getConferences(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (auth != null) {
			return ResponseEntity.ok(conferenceRepository.getForUser(auth.getId(), new PageRequest(page, size)));
		} else {
			return ResponseEntity.ok(conferenceRepository.getPublic(new PageRequest(page, size)));
		}
	}
}
