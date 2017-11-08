package com.pancisin.bookster.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.ArticlesList;
import com.pancisin.bookster.repository.ArticlesListRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticlesListRepository alRepository;

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
}
