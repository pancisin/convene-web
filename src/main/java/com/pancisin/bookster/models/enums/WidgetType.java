package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WidgetType {
	LATEST_ACTIVITY("LATEST_ACTIVITY", "admin.widgets.latest_activity", "latest-activity"),
	FOLLOWERS("FOLLOWERS", "admin.widgets.followers", "followers");

	private final String name;
	private final String code;
	private final String component;

	WidgetType(String name, String code, String component) {
		this.name = name;
		this.code = code;
		this.component = component;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getComponent() {
		return component;
	}
}
