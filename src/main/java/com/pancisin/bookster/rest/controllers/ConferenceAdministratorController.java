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

import com.pancisin.bookster.models.ConferenceAdministrator;
import com.pancisin.bookster.repository.ConferenceAdministratorRepository;

@RestController
@RequestMapping("/api/conference-administrator/{ca_id}")
@PreAuthorize("hasPermission(#ca_id, 'conference-administrator', 'update')")
public class ConferenceAdministratorController {

	@Autowired
	private ConferenceAdministratorRepository caRepository;

	@GetMapping
	public ResponseEntity<?> getPageAdministrator(@PathVariable Long ca_id) {
		return ResponseEntity.ok(caRepository.findOne(ca_id));
	}

	@PutMapping
	public ResponseEntity<?> putAdministrator(@PathVariable Long ca_id, @RequestBody ConferenceAdministrator ca) {
		ConferenceAdministrator stored = caRepository.findOne(ca_id);

		stored.setRole(ca.getRole());
		stored.setActive(ca.isActive());

		return ResponseEntity.ok(caRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deletePageAdministrator(@PathVariable Long ca_id) {
		caRepository.delete(ca_id);
		return ResponseEntity.ok("success");
	}
}
