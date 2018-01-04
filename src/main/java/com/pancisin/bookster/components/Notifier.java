package com.pancisin.bookster.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.model.Notification;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.repository.NotificationRepository;

@Component
public class Notifier {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate webSocket;

	public void notifyUser(User user, String code) {
		this.notifyUser(user, code, "");
	}

	public void notifyUser(User user, String code, String target) {
		Notification notification = new Notification(code, target);
		this.notifyUser(user, notification);
	}

	public void notifyUser(User user, String code, String object, String subject) {
		Notification notification = new Notification(code, object, subject);
		this.notifyUser(user, notification);
	}

	public void notifyUser(User user, Notification notification) {
		notification.setRecipient(user);
		webSocket.convertAndSendToUser(user.getEmail(), "/queue/notifier", notificationRepository.save(notification));
	}
}
