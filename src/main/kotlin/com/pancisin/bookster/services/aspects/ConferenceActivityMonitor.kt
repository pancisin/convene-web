package com.pancisin.bookster.services.aspects

import javax.transaction.Transactional

import com.pancisin.bookster.model.*
import com.pancisin.bookster.repository.ConferenceRepository
import com.pancisin.bookster.repository.PageRepository
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import com.pancisin.bookster.services.NotificationService
import com.pancisin.bookster.components.annotations.ActivityLog
import com.pancisin.bookster.model.enums.ActivityType
import com.pancisin.bookster.model.enums.ObjectType
import com.pancisin.bookster.repository.ActivityRepository
import com.pancisin.bookster.repository.UserRepository

@Aspect
@Component
class ConferenceActivityMonitor {

  @Autowired
  lateinit var activityRepository: ActivityRepository

  @Autowired
  lateinit var notificationService: NotificationService

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var conferenceRepository: ConferenceRepository

  @Pointcut("execution(* com.pancisin.bookster.rest.controllers.v1.ConferenceController.*(..)) && args(conference_id,..)")
  fun conferenceController(conference_id: Long?) {

  }

  @Transactional
  @AfterReturning(pointcut = "conferenceController(conference_id) && @annotation(activityLog)", returning = "response")
  fun logConferenceActivity(conference_id: Long?, activityLog: ActivityLog, response: ResponseEntity<*>) {
    val stored = conferenceRepository.findOne(conference_id)
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    val activity = Activity(user = auth, type = activityLog.type, page = stored)

    when (activityLog.type) {
      ActivityType.ATTENDING -> {
        val user = userRepository.findOne(auth.id)

        activity.apply {
          objectType = ObjectType.USER
          objectId = user.id.toString()
        }

        stored.administrators!!.stream().forEach { (_, user1) -> notificationService.notifyUser(user1!!, "notification.conference.new_attender", user.displayName, stored.displayName) }
      }
      ActivityType.CREATE_EVENT -> {
        val event = response.body as Event

        activity.apply {
          objectType = ObjectType.EVENT
          objectId = event.id.toString()
        }

        stored.members.stream().forEach { (_, user) -> notificationService.notifyUser(user!!, "notification.conference.event_created", event.name!!, stored.displayName) }
      }
      ActivityType.CREATE_SURVEY -> {
        val survey = response.body as Survey

        activity.apply {
          objectType = ObjectType.SURVEY
          objectId = survey.id.toString()
        }

        stored.members.stream().forEach { (_, user) -> notificationService.notifyUser(user!!, "notification.conference.survey_created", survey.name!!, stored.displayName) }
      }
      ActivityType.CREATE_ARTICLE -> {
        val article = response.body as Article

        activity.apply {
          objectType = ObjectType.ARTICLE
          objectId = article.id.toString()
        }

        stored.members.stream().forEach { (_, user) -> notificationService.notifyUser(user!!, "notification.conference.article_created", article.title!!, stored.displayName) }
      }
      else -> {

      }
    }

    activityRepository.save(activity)
  }
}
