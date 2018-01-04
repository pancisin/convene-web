package com.pancisin.bookster.components.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pancisin.bookster.model.enums.Subscription;

@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface License {
	Subscription value();
	String parent() default "";
	String parentId() default "";
}
