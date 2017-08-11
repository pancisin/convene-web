package com.pancisin.bookster.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.views.Summary;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping(value = "api/user/{user_id}")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@JsonView(Summary.class)
	@GetMapping("")
	public ResponseEntity<?> getUserData(@PathVariable Long user_id) {
		return ResponseEntity.ok(userRepository.findOne(user_id));
	}

	@GetMapping("event")
	public ResponseEntity<?> getEvents(@PathVariable Long user_id) {
		User stored = userRepository.findOne(user_id);
		return ResponseEntity.ok(stored.getEvents());
	}

	@JsonView(Summary.class)
	@GetMapping("/page")
	public ResponseEntity<?> getPage(@PathVariable Long user_id) {
		User stored = userRepository.findOne(user_id);
		return ResponseEntity.ok(stored.getPages());
	}

	@GetMapping("/followed-pages")
	@JsonView(Summary.class)
	public ResponseEntity<?> getFollowedPages(@PathVariable Long user_id) {
		User stored = userRepository.findOne(user_id);
		return ResponseEntity.ok(stored.getFollowed());
	}

}
