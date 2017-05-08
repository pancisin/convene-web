package com.pancisin.bookster.security.evaluators;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.pancisin.bookster.models.Event;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.UserRepository;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public EventRepository eventRepository;
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		System.out.println("test");
		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		
		User stored = null;
		if (authentication.getClass() != AnonymousAuthenticationToken.class) {
			User user = (User) authentication.getPrincipal();
			stored = userRepository.findOne(user.getId());
		}

		switch (targetType) {
		case "event":
			Event event = eventRepository.findOne((Long)targetId);
			return event.getVisibility() == Visibility.PUBLIC || (stored != null && event.getOwner().getId() == stored.getId());
//		case "customer": 
//			return company.getCustomers().stream().anyMatch(c -> c.getId() == targetId);
//		case "message":
//			return true;
//		case "license":
//			return stored.getLicenses().stream().anyMatch(l -> l.getId() == targetId);
		}

		return true;
	}
}
