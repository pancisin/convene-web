package com.pancisin.bookster.components.aspects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GeoLocationMonitor {

	@AfterReturning("execution(* com.pancisin.bookster..UserController.*(..)) && args(request)")
	public void logUserGeolocation(JoinPoint joinPoint, HttpServletRequest request) {
		System.out.println(request.getRemoteAddr());
	}
}
