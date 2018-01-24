package com.pancisin.bookster.components.aspects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;

import com.pancisin.bookster.repository.ConferenceRepository;
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

import com.pancisin.bookster.components.annotations.License;
import com.pancisin.bookster.components.annotations.LicenseLimit;
import com.pancisin.bookster.model.Page;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;

@Aspect
@Component
public class LicenseLimiter {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PageRepository pageRepository;

  @Autowired
  private ConferenceRepository conferenceRepository;

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
          if (stored.getLicense().getSubscription().getEventLimit() > stored.getEvents().stream()
            .filter(e -> e.getDate().after(Calendar.getInstance())).count())
            return pjp.proceed();
          break;
        case "page":
          if (stored.getLicense().getSubscription().getPageLimit() > pageRepository.getByOwner(auth_user.getId()).size())
            return pjp.proceed();
          break;
        case "conference":
          if (stored.getLicense().getSubscription().getConferenceLimit() > conferenceRepository.getByOwner(auth_user.getId()).size())
            return pjp.proceed();
      }
    } else if (limit.parent().equals("page")) {
      try {
        Page page = pageRepository.findOne((Long) pjp.getArgs()[getPathVariableIndex(method, limit.parentId())]);

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
      if (Arrays.stream(method.getParameterAnnotations()[i])
        .anyMatch(x -> x.annotationType() == PathVariable.class && ((PathVariable) x).value().equals(argName))) {
        return i;
      }
    }

    throw new Exception("Invalid argument");
  }

  @Around("@annotation(com.pancisin.bookster.components.annotations.License)")
  public Object licenseCheck(ProceedingJoinPoint pjp) throws Throwable {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User auth_user = (User) auth.getPrincipal();
    User stored = userRepository.findOne(auth_user.getId());

    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();

    License subscription = method.getAnnotation(License.class);

    if (subscription.parent().equals("") || subscription.parent().equals("user")) {
      if (stored.getLicense().getSubscription() == subscription.value()) {
        return pjp.proceed();
      }
    } else if (subscription.parent().equals("page")) {
      Page page = pageRepository.findOne((Long) pjp.getArgs()[getPathVariableIndex(method, subscription.parentId())]);

      if (page.getOwner().getLicense().getSubscription() == subscription.value()) {
        return pjp.proceed();
      }
    } else if (subscription.parent().equals("conference")) {
      Page conference = pageRepository.findOne((Long) pjp.getArgs()[getPathVariableIndex(method, subscription.parentId())]);

      if (conference.getOwner().getLicense().getSubscription() == subscription.value()) {
        return pjp.proceed();
      }
    }

    return new ResponseEntity<String>("Your current subscription do not allows this.", HttpStatus.PAYMENT_REQUIRED);
  }
}
