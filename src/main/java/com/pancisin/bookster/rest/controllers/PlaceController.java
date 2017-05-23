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

import com.pancisin.bookster.models.Place;
import com.pancisin.bookster.repository.PlaceRepository;

@RestController
@RequestMapping("/api/place/{place_id}")
public class PlaceController {

	@Autowired
	private PlaceRepository placeRepository;

	@GetMapping
	public ResponseEntity<?> getPlace(@PathVariable Long place_id) {
		Place place = placeRepository.findOne(place_id);
		return ResponseEntity.ok(place);
	}

	@DeleteMapping
	// @PreAuthorize("hasPermission(#place_id, 'place', 'update')")
	public ResponseEntity<?> deletePlace(@PathVariable Long place_id) {
		placeRepository.delete(place_id);
		return ResponseEntity.ok("success");
	}

	@PutMapping
	// @PreAuthorize("hasPermission(#place_id, 'place', 'update')")
	public ResponseEntity<?> putPlace(@PathVariable Long place_id, @RequestBody Place place) {
		Place stored = placeRepository.findOne(place_id);
		stored.setAddress(place.getAddress());
		stored.setCapacity(place.getCapacity());
		stored.setDescription(place.getDescription());
		stored.setName(place.getName());
		return ResponseEntity.ok(placeRepository.save(stored));
	}
}
