package com.pancisin.bookster.rest.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.PageRepository;

@RestController
@PreAuthorize("hasPermission(#page_id, 'page', '')")
@RequestMapping("/api/page/{page_id}")
public class PageController {

	@Autowired
	private PageRepository pageRepository;

	@GetMapping
	public ResponseEntity<?> getPage(@PathVariable Long page_id) {
		return ResponseEntity.ok(pageRepository.findOne(page_id));
	}

	@DeleteMapping
	public ResponseEntity<?> deletePage(@PathVariable Long page_id) {
		pageRepository.delete(page_id);
		return ResponseEntity.ok("success");
	}

	@PutMapping
	public ResponseEntity<?> deletePage(@PathVariable Long page_id, @RequestBody Page page) {
		Page stored = pageRepository.findOne(page_id);
		stored.setName(page.getName());
		stored.setCategory(page.getCategory());
		return ResponseEntity.ok(pageRepository.save(stored));
	}

	@PatchMapping("/toggle-follow")
	public ResponseEntity<?> followPage(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		boolean status = stored.getFollowers().stream().anyMatch(x -> x.getId() == user.getId());
		if (status)
			stored.setFollowers(
					stored.getFollowers().stream().filter(x -> x.getId() != user.getId()).collect(Collectors.toList()));
		else
			stored.getFollowers().add(user);

		pageRepository.save(stored);
		return ResponseEntity.ok(!status);
	}

	@GetMapping("/follow-status")
	public ResponseEntity<?> getFollowStatus(@PathVariable Long page_id) {
		Page stored = pageRepository.findOne(page_id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(stored.getFollowers().stream().anyMatch(x -> x.getId() == user.getId()));
	}
}
