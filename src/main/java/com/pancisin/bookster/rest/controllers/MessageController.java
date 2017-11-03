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
import com.pancisin.bookster.models.enums.RecipientType;
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
	
	@JsonView(Compact.class)
	@GetMapping("/page/{page_id}/{page}")
	public ResponseEntity<?> getPageConversations(@PathVariable Long page_id, @PathVariable int page) {
		return ResponseEntity.ok(messageRepository.getPageMessages(page_id, new PageRequest(page, pageLimit)));
	}

	@JsonView(Compact.class)
	@GetMapping("/event/{event_id}/{page}")
	public ResponseEntity<?> getEventConversations(@PathVariable Long event_id, @PathVariable int page) {
		return ResponseEntity.ok(messageRepository.getEventMessages(event_id, new PageRequest(page, pageLimit)));
	}
	
	@MessageMapping("/chat.private.{username}")
	@SendTo("/user/{username}/queue/chat.message")
	public Message sendDirectMessage(@Payload Message message, @DestinationVariable("username") String username, Principal principal) {
		User recipient = userRepository.findByEmail(username);

		message.setSender((User) principal);
		message.setRecipientType(RecipientType.USER);
		message.setRecipientId(recipient.getId());
		
		return messageRepository.save(message);
	}
	
	@MessageMapping("/chat.page.{page_id}")
	@SendTo("/topic/page/{page_id}/chat")
	public Message sendMessageToPage(@Payload Message message, @DestinationVariable("page_id") Long page_id, Principal principal) {
		
		message.setSender((User) principal);
		message.setRecipientType(RecipientType.PAGE);
		message.setRecipientId(page_id);
		
		return messageRepository.save(message);
	}

	@MessageMapping("/chat.event.{event_id}")
	@SendTo("/topic/event/{event_id}/chat")
	public Message sendMessageToEvent(@Payload Message message, @DestinationVariable("event_id") Long event_id, Principal principal) {
		
		message.setSender((User) principal);
		message.setRecipientType(RecipientType.EVENT);
		message.setRecipientId(event_id);
		
		return messageRepository.save(message);
	}
}
