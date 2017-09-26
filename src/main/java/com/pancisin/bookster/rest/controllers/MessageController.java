package com.pancisin.bookster.rest.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.Message;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.repository.MessageRepository;
import com.pancisin.bookster.repository.UserRepository;

@RestController
@RequestMapping("/api/message")
public class MessageController {

	private final int pageLimit = 10;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;
	
	@JsonView(Compact.class)
	@GetMapping("/user/{user_id}/{page}")
	public ResponseEntity<?> getPrivateConversation(@PathVariable Long user_id, @PathVariable int page) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		return ResponseEntity
				.ok(messageRepository.getPrivate(auth_user.getId(), user_id, new PageRequest(page, pageLimit)));
	}
	
	@MessageMapping("/chat.private.{username}")
	@SendTo("/user/{username}/queue/chat.message")
	public Message sendDirectMessage(@Payload Message message, @DestinationVariable("username") String username, Principal principal) {
		User recipient = userRepository.findByEmail(username);

		User new_rec = new User();
		new_rec.setEmail(recipient.getEmail());
		new_rec.setId(recipient.getId());
		
		message.setSender((User) principal);
		message.setRecipient(new_rec);
		
		return messageRepository.save(message);
	}
}
