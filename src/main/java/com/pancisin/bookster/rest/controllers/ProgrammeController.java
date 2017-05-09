package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.ProgrammeRepository;

@RestController
@PreAuthorize("hasPermission(#programme_id, 'programme', '')")
@RequestMapping("/api/programme/{programme_id}")
public class ProgrammeController {

	@Autowired
	private ProgrammeRepository programmeRepository;
	
	@DeleteMapping
	public ResponseEntity<?> deleteProgramme(@PathVariable Long programme_id) {
		programmeRepository.delete(programme_id);
		return ResponseEntity.ok("success");
	}
}
