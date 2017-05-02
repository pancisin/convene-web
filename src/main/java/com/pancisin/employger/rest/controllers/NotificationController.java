package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Notification;
import com.pancisin.employger.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notification/{notification_id}")
public class NotificationController {

	@Autowired
	private NotificationRepository notificationRepository;

	@GetMapping
	public ResponseEntity<?> getNotification(@PathVariable Long notification_id) {
		return ResponseEntity.ok(notificationRepository.findOne(notification_id));
	}

	@PatchMapping("/toggle-seen")
	public ResponseEntity<?> toggleSeen(@PathVariable Long notification_id) {
		Notification not = notificationRepository.findOne(notification_id);
		not.setSeen(!not.isSeen());
		return ResponseEntity.ok(notificationRepository.save(not));
	}
}
