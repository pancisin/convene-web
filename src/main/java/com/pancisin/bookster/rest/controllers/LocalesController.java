package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.LocaleRepository;

@RestController
@RequestMapping("/api/locales")
public class LocalesController {

	@Autowired
	private LocaleRepository localeRepository;

	@GetMapping
	public ResponseEntity<?> getPublic() {
		return ResponseEntity.ok(localeRepository.findAll());
	}
}
