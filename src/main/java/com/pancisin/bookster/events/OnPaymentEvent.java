package com.pancisin.bookster.events;

import org.springframework.context.ApplicationEvent;

import com.pancisin.bookster.model.UserSubscription;

public class OnPaymentEvent extends ApplicationEvent {

	private PaymentState state;
	private UserSubscription us;

	public OnPaymentEvent(UserSubscription us, PaymentState state) {
		super(us);
		this.us = us;
		this.state = state;
	}

	public enum PaymentState {
		SUCCESS,
		ERROR,
	}

	public PaymentState getState() {
		return state;
	}

	public UserSubscription getUs() {
		return us;
	}
}
