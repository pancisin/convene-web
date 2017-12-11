package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Summary;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Locale {
	sk("sk", "locale.sk", "DD.MM.YYYY", "HH:mm"), en("en", "locale.en", "MM/DD/YYYY", "H mm a");

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

	@JsonView(Summary.class)
	public String getName() {
		return name;
	}

	@JsonView(Summary.class)
	public String getCode() {
		return code;
	}

	@JsonView(Summary.class)
	public String getDateFormat() {
		return dateFormat;
	}

	@JsonView(Summary.class)
	public String getTimeFormat() {
		return timeFormat;
	}
}
