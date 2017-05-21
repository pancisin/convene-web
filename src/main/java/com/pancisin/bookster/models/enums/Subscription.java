package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Subscription {
	FREE("FREE", "subscription.free", 0, 2, 0, 0, 0), STARTER("STARTER", "subscription.starter", 9, 20, 1, 3,
			0), PROFESSIONAL("PROFESSIONAL", "subscription.professional", 29, 100, 10, 100,
					0), ENTERPRISE("ENTERPRISE", "subscription.enterprise", 89, 100, 100, 100, 100);

	Subscription(String name, String code, int price, int eventLimit, int pageLimit, int serviceLimit,
			int conferenceLimit) {
		this.price = price;
		this.code = code;
		this.name = name;
		this.eventLimit = eventLimit;
		this.pageLimit = pageLimit;
		this.serviceLimit = serviceLimit;
		this.conferenceLimit = conferenceLimit;
	}

	private final String name;
	private final String code;
	private final int price;

	private final int eventLimit;
	private final int pageLimit;
	private final int serviceLimit;
	private final int conferenceLimit;

	public int getPrice() {
		return price;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getEventLimit() {
		return eventLimit;
	}

	public int getPageLimit() {
		return pageLimit;
	}

	public int getServiceLimit() {
		return serviceLimit;
	}

	public int getConferenceLimit() {
		return conferenceLimit;
	}
}
