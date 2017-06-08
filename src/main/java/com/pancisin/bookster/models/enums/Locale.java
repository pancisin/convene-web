package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Locale {
	sk("SLOVAK", "sk", "DD.MM.YYYY", "HH:mm"), en("ENGLISH", "en", "MM/DD/YYYY", "H mm a");

	Locale(String name, String code, String dateFormat, String timeFormat) {
		this.name = name;
		this.code = code;
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
	}

	final String name;
	final String code;
	final String dateFormat;
	final String timeFormat;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}
}
