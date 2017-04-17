package com.pancisin.employger.websocket.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.pancisin.employger.models.Notification;

@Controller
public class NotificationController {

	@Autowired
    private SimpMessagingTemplate template;

	
//	@MessageMapping("/hello")
//	@SendTo("/topic/chat")
//	public Message<T> greeting(Message message, Principal principal) throws Exception {
//		message.setUser(new User(principal.getName()));
//		return message;
//	}
//	
//	@SendTo(value = "/notification")
//	public Notification notifyCompany(Notification notification) {
//		return notification;
//	}
//	
//	public void sendNotification(Notification notification) {
//		this.template.convertAndSend("/");
//	}

}
