package com.pancisin.bookster.rest.controllers;

import com.pancisin.bookster.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.components.storage.StorageService;
import com.pancisin.bookster.model.Place;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.repository.MediaRepository;
import com.pancisin.bookster.repository.PlaceRepository;

@RestController
@RequestMapping("/api/place/{place_id}")
public class PlaceController {

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private MediaRepository mediaRepository;

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

//		stored.setAddress(place.getAddress());
		stored.setCapacity(place.getCapacity());
		stored.setDescription(place.getDescription());
		stored.setName(place.getName());

		if (place.getVenueData() != null) {
			String url = "places/" + stored.getId();
			storageService.storeText(place.getVenueData(), url, "json");
			stored.setVenueJsonUrl("/files/" + url + ".json");
		}

		return ResponseEntity.ok(placeRepository.save(stored));
	}

	@PatchMapping("/venue")
	public ResponseEntity<?> patchVenue(@PathVariable Long place_id, @RequestBody String venue_data) {
		Place stored = placeRepository.findOne(place_id);

		if (venue_data != null) {
			String url = "places/" + stored.getId();
			storageService.storeText(venue_data, url, "json");
			stored.setVenueJsonUrl("/files/" + url + ".json");

			stored = placeRepository.save(stored);

			return ResponseEntity.ok(stored.getVenueJsonUrl());
		}

		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/gallery")
	public ResponseEntity<?> getGallery(@PathVariable Long place_id) {
		return ResponseEntity.ok(mediaRepository.getByPlace(place_id));
	}

	@PostMapping("/gallery")
	public ResponseEntity<?> postGallery(@PathVariable Long place_id, @RequestBody Media galleryItem) {
		Place stored = placeRepository.findOne(place_id);

		if (storageService.isBinary(galleryItem.getData())) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			galleryItem.setAuthor(user);

			galleryItem = mediaRepository.save(galleryItem);
			String url = "images/places/" + galleryItem.getId().toString();

			galleryItem.setPath("/files/" + url + ".jpg");
			Long size = storageService.storeBinary(galleryItem.getData(), url);
			galleryItem.setSize(size);
			stored.AddGallery(galleryItem);
		}

		placeRepository.save(stored);

		return ResponseEntity.ok(galleryItem);
	}
}
