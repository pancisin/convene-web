package com.pancisin.bookster.websocket.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.Notification;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.NotificationRepository;

@Component
public class Notifier {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate webSocket;

	public void notifyUser(User user, String title, String message) {
		Notification notification = new Notification(title, message);
		notification.setRecipient(user);
		webSocket.convertAndSendToUser(user.getEmail(), "/queue/notifier", notificationRepository.save(notification));
	}
}
