package com.pancisin.employger.websocket.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Notification;
import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.NotificationRepository;

@Component
public class Notifier {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate webSocket;

	public void notifyCompany(Company company, String title, String message) {
		Notification notification = new Notification(title, message);

		company.getUsers().stream().forEach(u -> {
			notification.setRecipient(u);
			webSocket.convertAndSendToUser(u.getEmail(), "/queue/notifier",
					notificationRepository.save(notification));
		});
	}

	public void notifyUser(User user, String title, String message) {
		Notification notification = new Notification(title, message);
		notification.setRecipient(user);
		webSocket.convertAndSendToUser(user.getEmail(), "/queue/notifier",
				notificationRepository.save(notification));
	}
}
