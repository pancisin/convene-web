package com.pancisin.bookster.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.GeoLocation;
import facebook4j.Place;
import facebook4j.Reading;
import facebook4j.ResponseList;

@RestController
@RequestMapping("/api/facebook-importer")
public class FacebookImporterController {

	@GetMapping("/search")
	public ResponseEntity<?> searchPages(@RequestParam(name = "latitude", required = true) Double latitude,
			@RequestParam(name = "longitude", required = true) Double longitude) {
		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());
			
			Reading r = new Reading();
			r.fields("name", "categories", "location", "metadata");
			
			ResponseList<Place> places = fb.searchPlaces("*", new GeoLocation(latitude, longitude), 1000, r);
			return ResponseEntity.ok(places);
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return null;
	}
}
