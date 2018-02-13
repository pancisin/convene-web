package com.pancisin.bookster.services

import com.pancisin.bookster.model.Activity
import com.pancisin.bookster.repository.ActivityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ActivityFeedService {

  @Autowired
  lateinit var webSocket: SimpMessagingTemplate

  @Autowired
  lateinit var activityRepository: ActivityRepository

  fun publishActivity(activity: Activity) {
    val act = activityRepository.save(activity)

    if (act.page != null) {
      act.page!!.members.forEach { m ->
        webSocket.convertAndSendToUser(m.user?.email, "/queue/activity", act)
      }
    } else if (act.event != null) {
      act.event!!.attendees.forEach { u ->
        webSocket.convertAndSendToUser(u.email, "/queue/activity", act)
      }
    }
  }
}
