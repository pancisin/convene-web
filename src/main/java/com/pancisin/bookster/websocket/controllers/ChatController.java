package com.pancisin.bookster.websocket.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.Message;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.repository.MessageRepository;
import com.pancisin.bookster.repository.UserRepository;

@Controller
public class ChatController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/chat")
	@SendTo("/queue/chat")
	public Message sendPublic(@Payload Message message, Principal principal) throws Exception {
		message.setSender((User) principal);
		Message stored = messageRepository.findOne(messageRepository.save(message).getId());
		return stored;
	}

	@MessageMapping("/chat.private.{username}")
	public void sendPrivate(@Payload Message message, @DestinationVariable("username") String username,
			Principal principal) {
		User recipient = userRepository.findByEmail(username);
		User new_rec = new User();
		new_rec.setEmail(recipient.getEmail());
		new_rec.setId(recipient.getId());
		
		message.setSender((User) principal);
		message.setRecipient(new_rec);

		simpMessagingTemplate.convertAndSendToUser(username, "/queue/chat.message", messageRepository.save(message));
//		simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/chat.message", messageRepository.save(message));
	}
}
