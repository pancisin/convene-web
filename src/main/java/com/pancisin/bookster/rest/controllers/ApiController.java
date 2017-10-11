package com.pancisin.bookster.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.ArticlesList;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.ArticlesListRepository;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticlesListRepository alRepository;

	@Autowired
	private ArticleRepository articleRepository;
	
	@GetMapping("/events/{page}/{size}")
	public ResponseEntity<?> getEvents(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return null;
	}

	@GetMapping("/conferences/{page}/{size}")
	public ResponseEntity<?> getConferences(@PathVariable int page, @PathVariable int size) {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(conferenceRepository.getForUser(auth.getId(), new PageRequest(page, size)));
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> getUsers(@RequestParam int page, @RequestParam int size) {
		return ResponseEntity.ok(userRepository.findAll(new PageRequest(page, size)));
	}

	@PostMapping("/articles-list")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> postArticlesList(@RequestBody @Valid ArticlesList articlesList) {
		return ResponseEntity.ok(alRepository.save(articlesList));
	}

	@GetMapping("/articles-list")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<?> getArticlesLists() {
		return ResponseEntity.ok(alRepository.findAll());
	}
	
	@GetMapping("/articles")
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
			Page<Article> articles = articleRepository.getByArticlesList(articlesList.getId(), new PageRequest(page, size));
			return ResponseEntity.ok(articles);
		}
		
		return null;
	}
}
