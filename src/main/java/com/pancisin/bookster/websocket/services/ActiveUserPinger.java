package com.pancisin.bookster.websocket.services;

import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActiveUserPinger {

	private SimpMessagingTemplate template;
	private ActiveUserService activeUserService;

	public ActiveUserPinger(SimpMessagingTemplate template, ActiveUserService activeUserService) {
		this.template = template;
		this.activeUserService = activeUserService;
	}

	@Scheduled(fixedDelay = 10000)
	public void pingUsers() {
		Set<String> activeUsers = activeUserService.getActiveUsers();
		template.convertAndSend("/topic/active", activeUsers);
	}
}