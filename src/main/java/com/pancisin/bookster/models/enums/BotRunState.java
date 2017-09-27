package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BotRunState {
	SCHEDULED("SCHEDULED", "bot.state.scheduled"),
	RUNNING("RUNNING", "bot.state.running"),
	SUCCESS("SUCCESS", "bot.state.success"),
	ERROR("ERROR", "bot.state.error"),
	UNKNOWN("UNKNOWN", "bot.state.unknown");

	final String name;
	final String code;

	BotRunState(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}
