package com.pancisin.bookster.rest.controllers.v1

import java.util.UUID

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.Notification
import com.pancisin.bookster.repository.NotificationRepository

@RestController
@PreAuthorize("hasPermission(#notification_id, 'notification', 'read')")
@RequestMapping("/api/v1/notification/{notification_id}")
class NotificationController {

  @Autowired
  lateinit var notificationRepository: NotificationRepository

  @GetMapping
  fun getNotification(@PathVariable notification_id: UUID) = ResponseEntity.ok(notificationRepository.findOne(notification_id))

  @PatchMapping("/toggle-seen")
  fun toggleSeen(@PathVariable notification_id: UUID): ResponseEntity<Notification> {
    val not = notificationRepository.findOne(notification_id).apply {
      seen = !seen
    }

    return ResponseEntity.ok(notificationRepository.save(not))
  }
}
