package com.pancisin.bookster.services.aspects

import com.pancisin.bookster.components.annotations.ActivityLog
import com.pancisin.bookster.model.Activity
import com.pancisin.bookster.model.Programme
import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.enums.ActivityType
import com.pancisin.bookster.model.enums.ObjectType
import com.pancisin.bookster.repository.ActivityRepository
import com.pancisin.bookster.repository.EventRepository
import com.pancisin.bookster.services.ActivityFeedService
import com.pancisin.bookster.services.NotificationService
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Aspect
@Component
class EventActivityMonitor {

  @Autowired
  private lateinit var eventRepository: EventRepository

  @Autowired
  private lateinit var notificationService: NotificationService

  @Autowired
  private lateinit var activityFeedService: ActivityFeedService

  @Pointcut("execution(* com.pancisin.bookster.rest.controllers.v1.EventController.*(..)) && args(event_id,..)")
  fun eventController(event_id: Long?) {
  }

  @Transactional
  @AfterReturning(pointcut = "eventController(event_id) && @annotation(activityLog)", returning = "response")
  fun logEventActivity(event_id: Long?, activityLog: ActivityLog, response: ResponseEntity<*>) {
    val stored = eventRepository.findOne(event_id)
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    val activity = Activity(user = auth, type = activityLog.type, event = stored)

    when (activityLog.type) {
      ActivityType.CREATE_PROGRAMME -> {
        val programme = response.body as Programme

        activity.apply {
          objectType = ObjectType.PROGRAMME
          objectId = programme.id.toString()
        }

        stored.attendees.forEach { notificationService.notifyUser(it, "notification.event.new_attender", it.displayName, stored.name!!) }
      }
      else -> {

      }
    }

    activityFeedService.publishActivity(activity)
  }
}
