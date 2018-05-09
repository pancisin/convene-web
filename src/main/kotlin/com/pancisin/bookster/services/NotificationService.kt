package com.pancisin.bookster.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.Notification
import com.pancisin.bookster.model.User
import com.pancisin.bookster.repository.NotificationRepository

@Component
class NotificationService {

  @Autowired
  lateinit var notificationRepository: NotificationRepository

  @Autowired
  lateinit var webSocket: SimpMessagingTemplate

  fun notifyUser(user: User, code: String, target: String) = this.notifyUser(user, Notification(code = code, target = target))

  fun notifyUser(user: User, code: String, target: String, subject: String) = this.notifyUser(user, Notification(code = code, target = target, subject = subject))

  fun notifyUser(user: User, notification: Notification) {
    notification.recipient = user
    webSocket.convertAndSendToUser(user.email, "/queue/notifier", notificationRepository.save(notification))
  }
}
