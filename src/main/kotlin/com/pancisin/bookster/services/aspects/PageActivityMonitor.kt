package com.pancisin.bookster.services.aspects

import javax.transaction.Transactional

import com.pancisin.bookster.model.*
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
import com.pancisin.bookster.repository.PageRepository
import com.pancisin.bookster.repository.UserRepository
import com.pancisin.bookster.services.ActivityFeedService

@Aspect
@Component
class PageActivityMonitor {

  @Autowired
  private lateinit var pageRepository: PageRepository

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var notificationService: NotificationService

  @Autowired
  private lateinit var activityFeedService: ActivityFeedService

  @Pointcut("execution(* com.pancisin.bookster.rest.controllers.PageController.*(..)) && args(page_id,..)")
  fun pageController(page_id: Long?) {
  }

  @Transactional
  @AfterReturning(pointcut = "pageController(page_id) && @annotation(activityLog)", returning = "response")
  fun LogPageActivity(page_id: Long?, activityLog: ActivityLog, response: ResponseEntity<*>) {
    val stored = pageRepository.findOne(page_id)
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    val activity = Activity(user = auth, type = activityLog.type, page = stored)

    when (activityLog.type) {
      ActivityType.CREATE_EVENT -> {
        val event = response.body as Event
        activity.apply {
          objectType = ObjectType.EVENT
          objectId = event.id.toString()
        }

        stored.members.forEach { (_, user) -> notificationService.notifyUser(user!!, "notification.page.event_created", event.name!!, stored.displayName) }
      }
      ActivityType.FOLLOWING -> {
        val user = userRepository.findOne(auth.id)
        activity.apply {
          objectType = ObjectType.USER
          objectId = user.id.toString()
        }

        stored.administrators!!.forEach { (_, user1) -> notificationService.notifyUser(user1!!, "notification.page.new_follower", user.displayName, stored.displayName) }
      }
      ActivityType.CREATE_SERVICE -> {
        val service = response.body as Service

        activity.apply {
          objectType = ObjectType.SERVICE
          objectId = service.id.toString()
        }

        stored.members.forEach { (_, user) -> notificationService.notifyUser(user!!, "notification.page.service_created", service.name!!, stored.displayName) }
      }
      ActivityType.CREATE_ADMINISTRATOR -> {
        val (_, user, _, active, _, _, role) = response.body as Administrator

        activity.apply {
          objectType = ObjectType.USER
          objectId = user!!.id.toString()
        }

        if (active!!) {
          notificationService.notifyUser(user!!, "notification.page.administrator_created", role!!.prop, stored.displayName)
        }
      }
      else -> {
      }
    }

    activityFeedService.publishActivity(activity)
  }
}
