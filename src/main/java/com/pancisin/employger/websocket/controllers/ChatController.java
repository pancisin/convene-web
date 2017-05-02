package com.pancisin.employger.websocket.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.pancisin.employger.models.Message;
import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.MessageRepository;
import com.pancisin.employger.repository.UserRepository;

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

		message.setSender((User) principal);
		message.setRecipient(recipient);

		simpMessagingTemplate.convertAndSendToUser(username, "/queue/chat.message", messageRepository.save(message));
//		simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/chat.message", messageRepository.save(message));
	}
}
