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
		case "company":
			return stored.getCompany().getId() == targetId;
		case "customer": 
			return stored.getCompany().getCustomers().stream().filter(c -> c.getId() == targetId).count() == 1;
		case "duty":
			return stored.getCompany().getDuties().stream().filter(d -> d.getId() == targetId).count() == 1;
		}

		return false;
	}
}
