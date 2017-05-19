package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Unit {
	HOUR("HOUR", "unit.hour"), MINUTE("MINUTE", "unit.minute"), SQUARED_METER("SQUARED_METER",
			"unit.squared_meter"), PIECE("PIECE", "unit.piece");

	Unit(String name, String code) {
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
