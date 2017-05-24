package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.repository.PageAdministratorRepository;

@RestController
@RequestMapping("/api/page-administrator/{pa_id}")
@PreAuthorize("hasPermission(#pa_id, 'page-administrator', 'update')")
public class PageAdministratorController {

	@Autowired
	private PageAdministratorRepository paRepository;

	@GetMapping
	public ResponseEntity<?> getPageAdministrator(@PathVariable Long pa_id) {
		return ResponseEntity.ok(paRepository.findOne(pa_id));
	}

	@PutMapping
	public ResponseEntity<?> putAdministrator(@PathVariable Long pa_id, @RequestBody PageAdministrator pa) {
		PageAdministrator stored = paRepository.findOne(pa_id);

		stored.setRole(pa.getRole());
		stored.setActive(pa.getActive());

		return ResponseEntity.ok(paRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deletePageAdministrator(@PathVariable Long pa_id) {
		paRepository.delete(pa_id);
		return ResponseEntity.ok("success");
	}
}
