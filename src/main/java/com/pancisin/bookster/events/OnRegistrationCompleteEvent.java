package com.pancisin.bookster.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.pancisin.bookster.model.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private String appUrl;
	private Locale locale;
	private User user;

	public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
		super(user);

		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public User getUser() {
		return user;
	}

	public String getAppUrl() {
		return appUrl;
	}
}
