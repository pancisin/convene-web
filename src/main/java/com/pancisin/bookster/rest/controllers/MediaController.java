package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import com.pancisin.bookster.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.repository.MediaRepository;

@RestController
@RequestMapping("/api/media/{media_id}")
public class MediaController {

	@Autowired
	private MediaRepository mediaRepository;

	@GetMapping
	public ResponseEntity<?> getMedia(@PathVariable UUID media_id) {
		return ResponseEntity.ok(mediaRepository.findById(media_id));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteMedia(@PathVariable UUID media_id) {
		Media stored = mediaRepository.findById(media_id);
		stored.setDeleted(true);
		return ResponseEntity.ok(mediaRepository.save(stored));
	}
}
