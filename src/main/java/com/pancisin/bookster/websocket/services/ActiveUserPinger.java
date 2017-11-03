package com.pancisin.bookster.websocket.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		List<UserStats> activeUsers = activeUserService.getActiveUsers();
		activeUsers.stream().forEach(user -> {
			Set<String> activeContacts = user.getContacts().stream()
					.filter(c -> activeUsers.stream().anyMatch(u -> u.getEmail().equals(c))).collect(Collectors.toSet());
			template.convertAndSendToUser(user.getEmail(), "/queue/chat.activeUsers", activeContacts);
		});
	}

	/// THIS SUBSCRIPTION MUST BE STRICTLY SECURED FOR SUPERADMIN ONLY.
	@Scheduled(fixedDelay = 10000)
	public void broadcastActiveUsers() {
		Set<String> activeUsers = activeUserService.getActiveUsers().stream().map(x -> x.getEmail())
				.collect(Collectors.toSet());
		template.convertAndSend("/topic/active", activeUsers);
	}
}