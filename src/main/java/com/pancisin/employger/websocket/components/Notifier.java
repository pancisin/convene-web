package com.pancisin.employger.websocket.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Notification;
import com.pancisin.employger.repository.NotificationRepository;

@Component
public class Notifier {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
//	@Scheduled(fixedRate = 5000)
	public void NotifiAll() {
		webSocket.convertAndSend("/queue/notifications", new Notification("Duty manipulation", "Duty has been updated"));
	}
	
	public void notifyCompany(Company company, String title, String message) {
		Notification notification = new Notification(title, message);
		notification.setRecipient(company);
		
		if (notificationRepository.save(notification) != null) {
			webSocket.convertAndSend("/queue/notifications", notification);
		};
	}
}
