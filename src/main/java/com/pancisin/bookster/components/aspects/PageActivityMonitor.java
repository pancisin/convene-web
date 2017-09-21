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
import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.Service;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.ActivityType;
import com.pancisin.bookster.repository.ActivityRepository;
import com.pancisin.bookster.repository.PageRepository;

@Aspect
@Component
public class PageActivityMonitor {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private Notifier notifier;

	@Pointcut("execution(* com.pancisin.bookster.rest.controllers.PageController.*(..)) && args(page_id,..)")
	public void pageController(Long page_id) {
	}

	@Transactional
	@AfterReturning(pointcut = "pageController(page_id) && @annotation(activityLog)", returning = "response")
	public void LogPageActivity(Long page_id, ActivityLog activityLog, ResponseEntity<?> response) {
		Page stored = pageRepository.findOne(page_id);

		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Activity activity = activityRepository.save(new Activity(auth, activityLog.type()));
		stored.addActivity(activity);

		if (activityLog.type() == ActivityType.CREATE_EVENT) {
			Event event = (Event) response.getBody();
			stored.getFollowers().stream().forEach(user -> {
				notifier.notifyUser(user, "notification.page.event_created", event.getName());
			});
		}

		if (activityLog.type() == ActivityType.FOLLOWING) {
			stored.getPageAdministrators().stream().forEach(x -> {
				notifier.notifyUser(x.getUser(), "notification.page.new_follower", auth.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_SERVICE) {
			Service service = (Service) response.getBody();

			stored.getFollowers().stream().forEach(user -> {
				notifier.notifyUser(user, "notification.page.service_created", service.getName());
			});
		}

		pageRepository.save(stored);
	}
}
