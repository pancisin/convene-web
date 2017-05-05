package com.pancisin.employger.security.evaluators;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pancisin.employger.models.User;
import com.pancisin.employger.repository.UserRepository;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	public UserRepository userRepository;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		System.out.println("test");
		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		User user = (User) authentication.getPrincipal();
		User stored = userRepository.findOne(user.getId());

		switch (targetType) {
		case "event":
			return stored.getEvents().stream().anyMatch(e -> e.getId() == targetId);
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
