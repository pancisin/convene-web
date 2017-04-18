package com.pancisin.employger.rest.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.MessageRepository;

@RestController
@RequestMapping("/api/message")
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@GetMapping("/")
	public ResponseEntity<?> getPublicConversation() {
		return ResponseEntity.ok("test");
	}

	@GetMapping("/user/{user_id}")
	public ResponseEntity<?> getPrivateConversation(@PathVariable Long user_id, Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		return ResponseEntity.ok(messageRepository.getPrivate(auth_user.getId(), user_id));
	}
}
