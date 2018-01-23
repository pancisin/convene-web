package com.pancisin.bookster.components.aspects;

import javax.transaction.Transactional;

import com.pancisin.bookster.model.*;
import com.pancisin.bookster.repository.PageRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.services.NotificationService;
import com.pancisin.bookster.components.annotations.ActivityLog;
import com.pancisin.bookster.model.enums.ActivityType;
import com.pancisin.bookster.repository.ActivityRepository;
import com.pancisin.bookster.repository.UserRepository;

@Aspect
@Component
public class ConferenceActivityMonitor {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
  private PageRepository pageRepository;

	@Pointcut("execution(* com.pancisin.bookster.rest.controllers.ConferenceController.*(..)) && args(conference_id,..)")
	public void conferenceController(Long conference_id) {

	}

	@Transactional
	@AfterReturning(pointcut = "conferenceController(conference_id) && @annotation(activityLog)", returning = "response")
	public void logConferenceActivity(Long conference_id, ActivityLog activityLog, ResponseEntity<?> response) {
		Page stored = pageRepository.findOne(conference_id);

		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Activity activity = new Activity(auth, activityLog.type(), stored);
		activity = activityRepository.save(activity);

		if (activityLog.type() == ActivityType.ATTENDING) {
			User user = userRepository.findOne(auth.getId());
			stored.getAdministrators().stream().forEach(ca -> {
				notificationService.notifyUser(ca.getUser(), "notification.conference.new_attender", user.getDisplayName(), stored.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_EVENT) {
			Event event = (Event) response.getBody();
			stored.getMembers().stream().forEach(att -> {
				notificationService.notifyUser(att.getUser(), "notification.conference.event_created", event.getName(), stored.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_SURVEY) {
			Survey survey = (Survey) response.getBody();
			stored.getMembers().stream().forEach(att -> {
				notificationService.notifyUser(att.getUser(), "notification.conference.survey_created", survey.getName(), stored.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_ARTICLE) {
			Article article = (Article) response.getBody();
			stored.getMembers().stream().forEach(att -> {
				notificationService.notifyUser(att.getUser(), "notification.conference.article_created", article.getTitle(), stored.getDisplayName());
			});
		}
	}
}
