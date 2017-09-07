package com.pancisin.bookster.components.aspects;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.components.annotations.ActivityLog;
import com.pancisin.bookster.models.Activity;
import com.pancisin.bookster.models.Page;
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
	
	@Pointcut("execution(* com.pancisin.bookster.rest.controllers.PageController.*(..)) && args(page_id,..)")
	public void pageController(Long page_id) {
		
	}

	@Transactional
	@AfterReturning("pageController(page_id) && @annotation(activityLog)")
	public void LogPageActivity(Long page_id, ActivityLog activityLog) {
		Page stored = pageRepository.findOne(page_id);
		
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Activity activity = activityRepository.save(new Activity(auth, activityLog.type()));
		stored.addActivity(activity);
		
		pageRepository.save(stored);
	}
}
