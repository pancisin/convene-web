package com.pancisin.bookster.rest.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.model.Notification;
import com.pancisin.bookster.repository.NotificationRepository;

@RestController
@PreAuthorize("hasPermission(#notification_id, 'notification', 'read')")
@RequestMapping("/api/notification/{notification_id}")
public class NotificationController {

	@Autowired
	private NotificationRepository notificationRepository;

	@GetMapping
	public ResponseEntity<?> getNotification(@PathVariable UUID notification_id) {
		return ResponseEntity.ok(notificationRepository.findOne(notification_id));
	}

	@PatchMapping("/toggle-seen")
	public ResponseEntity<?> toggleSeen(@PathVariable UUID notification_id) {
		Notification not = notificationRepository.findOne(notification_id);
		not.setSeen(!not.getSeen());
		return ResponseEntity.ok(notificationRepository.save(not));
	}
}
