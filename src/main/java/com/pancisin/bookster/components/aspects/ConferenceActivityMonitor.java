package com.pancisin.bookster.components.aspects;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.components.Notifier;
import com.pancisin.bookster.components.annotations.ActivityLog;
import com.pancisin.bookster.models.Activity;
import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Survey;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.ActivityType;
import com.pancisin.bookster.repository.ActivityRepository;
import com.pancisin.bookster.repository.ConferenceRepository;

@Aspect
@Component
public class ConferenceActivityMonitor {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private Notifier notifier;

	@Pointcut("execution(* com.pancisin.bookster.rest.controllers.ConferenceController.*(..)) && args(conference_id,..)")
	public void conferenceController(Long conference_id) {

	}

	@Transactional
	@AfterReturning(pointcut = "conferenceController(conference_id) && @annotation(activityLog)", returning = "response")
	public void logConferenceActivity(Long conference_id, ActivityLog activityLog, ResponseEntity<?> response) {
		Conference stored = conferenceRepository.findOne(conference_id);

		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Activity activity = activityRepository.save(new Activity(auth, activityLog.type()));
		stored.addActivity(activity);

		if (activityLog.type() == ActivityType.ATTENDING) {
			stored.getConferenceAdministrators().stream().forEach(ca -> {
				notifier.notifyUser(ca.getUser(), "notification.conference.new_attender");
			});
		}

		if (activityLog.type() == ActivityType.CREATE_EVENT) {
			Event event = (Event) response.getBody();
			stored.getAttendees().stream().forEach(att -> {
				notifier.notifyUser(att.getUser(), "notification.conference.event_created", event.getName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_SURVEY) {
			Survey survey = (Survey) response.getBody();
			stored.getAttendees().stream().forEach(att -> {
				notifier.notifyUser(att.getUser(), "notification.conference.survey_created", survey.getName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_ARTICLE) {
			Article article = (Article) response.getBody();
			stored.getAttendees().stream().forEach(att -> {
				notifier.notifyUser(att.getUser(), "notification.conference.article_created", article.getTitle());
			});
		}

		conferenceRepository.save(stored);
	}
}
