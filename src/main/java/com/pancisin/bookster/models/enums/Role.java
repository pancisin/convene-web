package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {
	ROLE_OWNER("ROLE_OWNER", "role.owner"), ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR", "role.administrator");

	Role(String name, String code) {
		this.name = name;
		this.code = code;
	}

	private final String code;
	private final String name;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
