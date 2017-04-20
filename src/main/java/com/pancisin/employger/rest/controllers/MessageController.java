package com.pancisin.employger.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.MessageRepository;
import com.pancisin.employger.repository.UserRepository;

@RestController
@RequestMapping("/api/message")
public class MessageController {

	private final int pageLimit = 10;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{page}")
	public ResponseEntity<?> getPublicConversation(@PathVariable int page) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		
		List<Long> users = userRepository.findOne(auth_user.getId()).getCompany().getUsers().stream().map(u -> u.getId()).collect(Collectors.toList());
		
		return ResponseEntity.ok(messageRepository.getPublicUser(users, new PageRequest(page, pageLimit)));
	}

	@GetMapping("/user/{user_id}/{page}")
	public ResponseEntity<?> getPrivateConversation(@PathVariable Long user_id, @PathVariable int page) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		return ResponseEntity
				.ok(messageRepository.getPrivate(auth_user.getId(), user_id, new PageRequest(page, pageLimit)));
	}
}
