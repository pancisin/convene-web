package com.pancisin.bookster.components.aspects;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.pancisin.bookster.components.annotations.LicenseLimit;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;

@Aspect
@Component
public class LicenseLimiter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PageRepository pageRepository;

	@Around("@annotation(com.pancisin.bookster.components.annotations.ActiveLicenseCheck)")
	public Object checkActiveLicense(ProceedingJoinPoint pjp) throws Throwable {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User stored = userRepository.findOne(auth.getId());

		if (stored.getOwningPages().size() > stored.getLicense().getSubscription().getPageLimit()) {
			return new ResponseEntity<String>("You've reached your resources limit.", HttpStatus.PAYMENT_REQUIRED);
		}

		return pjp.proceed();
	}

	@Around("@annotation(com.pancisin.bookster.components.annotations.LicenseLimit)")
	public Object limitUserResources(ProceedingJoinPoint pjp) throws Throwable {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User auth_user = (User) auth.getPrincipal();
		User stored = userRepository.findOne(auth_user.getId());

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		LicenseLimit limit = method.getAnnotation(LicenseLimit.class);

		if (limit.parent() == null || limit.parent().equals("") || limit.parent().equals("user")) {
			switch (limit.entity()) {
			case "event":
				if (stored.getLicense().getSubscription().getEventLimit() > stored.getEvents().size())
					return pjp.proceed();
				break;
			case "page":
				if (stored.getLicense().getSubscription().getPageLimit() > stored.getPages().size())
					return pjp.proceed();
				break;
			}
		} else if (limit.parent().equals("page")) {
			try {
				Page page = pageRepository
						.findOne((Long) pjp.getArgs()[getPathVariableIndex(method, limit.parentId())]);

				switch (limit.entity()) {
				case "event":
					if (page.getOwner().getLicense().getSubscription().getEventLimit() > page.getEvents().size())
						return pjp.proceed();
					break;
				case "service":
					if (page.getOwner().getLicense().getSubscription().getServiceLimit() > page.getServices().size())
						return pjp.proceed();
				}
			} catch (Exception ex) {

			}
		}

		return new ResponseEntity<String>("You've reached your resources limit.", HttpStatus.PAYMENT_REQUIRED);
	}

	private int getPathVariableIndex(Method method, String argName) throws Exception {
		for (int i = 0; i < method.getParameterAnnotations().length; i++) {
			if (Arrays.stream(method.getParameterAnnotations()[i]).anyMatch(
					x -> x.annotationType() == PathVariable.class && ((PathVariable) x).value().equals(argName))) {
				return i;
			}
		}

		throw new Exception("Invalid argument");
	}
}
