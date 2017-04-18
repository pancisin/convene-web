package com.pancisin.employger.websocket.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	@MessageMapping("/chat")
	@SendTo("/queue/chat")
	public Message greeting(Message message, Principal principal) throws Exception {
		message.setSender((User)principal);
		return messageRepository.save(message);
	}
}
