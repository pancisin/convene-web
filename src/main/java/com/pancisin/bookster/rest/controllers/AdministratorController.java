package com.pancisin.bookster.rest.controllers;

import com.pancisin.bookster.model.Administrator;
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

import com.pancisin.bookster.repository.AdministratorRepository;

@RestController
@RequestMapping("/api/administrator/{pa_id}")
@PreAuthorize("hasPermission(#pa_id, 'administrator', 'update')")
public class AdministratorController {

	@Autowired
	private AdministratorRepository administratorRepository;

	@GetMapping
	public ResponseEntity<?> getAdministrator(@PathVariable Long pa_id) {
		return ResponseEntity.ok(administratorRepository.findOne(pa_id));
	}

	@PutMapping
	public ResponseEntity<?> putAdministrator(@PathVariable Long pa_id, @RequestBody Administrator pa) {
		Administrator stored = administratorRepository.findOne(pa_id);

		stored.setRole(pa.getRole());
		stored.setActive(pa.getActive());

		return ResponseEntity.ok(administratorRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAdministrator(@PathVariable Long pa_id) {
		administratorRepository.delete(pa_id);
		return ResponseEntity.ok("success");
	}
}
