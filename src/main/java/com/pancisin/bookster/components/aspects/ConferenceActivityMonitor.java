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
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.ActivityRepository;
import com.pancisin.bookster.repository.ConferenceRepository;

@Aspect
@Component
public class ConferenceActivityMonitor {

	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	private ConferenceRepository conferenceRepository;
	
	@Pointcut("execution(* com.pancisin.bookster.rest.controllers.ConferenceController.*(..)) && args(conference_id,..)")
	public void conferenceController(Long conference_id) {
		
	}
	
	@Transactional
	@AfterReturning("conferenceController(conference_id) && @annotation(activityLog)")
	public void logConferenceActivity(Long conference_id, ActivityLog activityLog) {
		Conference stored = conferenceRepository.findOne(conference_id);
		
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Activity activity = activityRepository.save(new Activity(auth, activityLog.type()));
		stored.addActivity(activity);
		
		conferenceRepository.save(stored);
	}
}
