package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.PageRepository;

@RestController
@PreAuthorize("hasPermission(#page_id, 'page', '')")
@RequestMapping("/api/page/{page_id}")
public class PageController {

	@Autowired
	private PageRepository pageRepository;
	
	@GetMapping
	public ResponseEntity<?> getPage(@PathVariable Long page_id){
		return ResponseEntity.ok(pageRepository.findOne(page_id));
	}
}
