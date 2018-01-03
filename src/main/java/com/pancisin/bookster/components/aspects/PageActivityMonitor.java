package com.pancisin.bookster.components.aspects;

import java.util.HashMap;
import java.util.Map;

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
import com.pancisin.bookster.repository.UserRepository;

@Aspect
@Component
public class PageActivityMonitor {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private UserRepository userRepository;
	
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
		
		Activity activity = new Activity(auth, activityLog.type());
		activity.setPage(stored);
		activity = activityRepository.save(activity);

		if (activityLog.type() == ActivityType.CREATE_EVENT) {
			Event event = (Event) response.getBody();
			stored.getFollowers().stream().forEach(user -> {
				notifier.notifyUser(user, "notification.page.event_created", event.getName(), stored.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.FOLLOWING) {
			User user = userRepository.findOne(auth.getId());
			
			stored.getAdministrators().stream().forEach(x -> {
				notifier.notifyUser(x.getUser(), "notification.page.new_follower", user.getDisplayName(), stored.getDisplayName());
			});
		}

		if (activityLog.type() == ActivityType.CREATE_SERVICE) {
			Service service = (Service) response.getBody();

			stored.getFollowers().stream().forEach(user -> {
				notifier.notifyUser(user, "notification.page.service_created", service.getName(), stored.getDisplayName());
			});
		}
	}
}
