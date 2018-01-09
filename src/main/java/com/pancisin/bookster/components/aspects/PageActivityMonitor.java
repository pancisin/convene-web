package com.pancisin.bookster.components.aspects;

import javax.transaction.Transactional;

import com.pancisin.bookster.model.*;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.services.NotificationService;
import com.pancisin.bookster.components.annotations.ActivityLog;
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
	private NotificationService notificationService;

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

		switch (activityLog.type()) {
      case CREATE_EVENT:
        Event event = (Event) response.getBody();
        stored.getFollowers().stream().forEach(u -> {
          notificationService.notifyUser(u, "notification.page.event_created", event.getName(), stored.getDisplayName());
        });
        break;
      case FOLLOWING:
        User user = userRepository.findOne(auth.getId());
        stored.getAdministrators().stream().forEach(x -> {
          notificationService.notifyUser(x.getUser(), "notification.page.new_follower", user.getDisplayName(), stored.getDisplayName());
        });
        break;
      case CREATE_SERVICE:
        Service service = (Service) response.getBody();
        stored.getFollowers().stream().forEach(u -> {
          notificationService.notifyUser(u, "notification.page.service_created", service.getName(), stored.getDisplayName());
        });
        break;
      case CREATE_ADMINISTRATOR:
        Administrator admin = (Administrator) response.getBody();
        if (admin.getActive()) {
          notificationService.notifyUser(admin.getUser(), "notification.page.administrator_created", admin.getRole().getProp() ,stored.getDisplayName());
        }
        break;
      default:
        break;
    }
	}
}
