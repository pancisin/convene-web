package com.pancisin.bookster.components.annotations;

import com.pancisin.bookster.model.enums.PrivacyAccess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrivacyRestricted {
  public String constraint() default "";
}
