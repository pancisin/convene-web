package com.pancisin.bookster.components.aspects;

import com.pancisin.bookster.components.annotations.PrivacyRestricted;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.model.enums.PrivacyAccess;
import com.pancisin.bookster.repository.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PrivacyRestrictionWatcher {

  @Autowired
  private UserRepository userRepository;

  @Around("@annotation(restriction) && args(user_id,..)")
  public Object watchRestrictedMethods(ProceedingJoinPoint pjp, PrivacyRestricted restriction, Long user_id) throws Throwable {
    User stored = userRepository.findOne(user_id);

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    boolean self = !(principal instanceof String) && ((User) principal).getId().equals(user_id);

    if (stored.getPrivacyConstraints().containsKey(restriction.constraint())
      && stored.getPrivacyConstraints().get(restriction.constraint()) != PrivacyAccess.PUBLIC
      && !self) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    return pjp.proceed();
  }
}
