package com.pancisin.bookster.components.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pancisin.bookster.model.enums.ActivityType;

@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLog {
	public ActivityType type() default ActivityType.NOT_SPECIFIED;
}
