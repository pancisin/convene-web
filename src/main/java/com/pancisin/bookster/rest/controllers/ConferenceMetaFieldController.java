package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.ConferenceMetaField;
import com.pancisin.bookster.repository.ConferenceMetaFieldRepository;

@RestController
@RequestMapping("/api/conference-meta-field/{field_id}")
public class ConferenceMetaFieldController {

	@Autowired
	private ConferenceMetaFieldRepository cmfRepository;

	@GetMapping
	public ResponseEntity<?> getField(@PathVariable Long field_id) {
		return ResponseEntity.ok(cmfRepository.findOne(field_id));
	}

	@PutMapping
	public ResponseEntity<?> putField(@PathVariable Long field_id, @RequestBody ConferenceMetaField field) {
		ConferenceMetaField stored = cmfRepository.findOne(field_id);
		field.setConference(stored.getConference());
		return ResponseEntity.ok(cmfRepository.save(field));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteField(@PathVariable Long field_id) {
		cmfRepository.delete(field_id);
		return ResponseEntity.ok("success");
	}
}
