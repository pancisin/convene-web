package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SubscriptionState {
	NEW("NEW", "subscription.state.new"), PAID("PAID", "subscription.state.paid"), ACTIVE("ACTIVE",
			"subscription.state.active"), EXPIRED("EXPIRED",
					"subscription.state.expired"), UNPAID("UNPAID", "subscription.state.unpaid");

	SubscriptionState(String name, String code) {
		this.name = name;
		this.code = code;
	}

	private final String name;
	private final String code;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}
