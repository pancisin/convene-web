package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired 
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public ResponseEntity<?> users() {
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	@RequestMapping("/me")
	public ResponseEntity<?> me() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		User stored = userRepository.findOne(auth_user.getId());
		return ResponseEntity.ok(stored);
	}
}
