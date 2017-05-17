package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {
	ROLE_OWNER("ROLE_OWNER", "role.owner"), ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR",
			"role.administrator"), REPORTER("ROLE_REPORTER", "role.reporter");

	Role(String name, String code) {
		this.name = name;
		this.code = code;
	}

	private final String code;
	private final String name;

	@JsonView(Compact.class)
	public String getCode() {
		return code;
	}

	@JsonView(Compact.class)
	public String getName() {
		return name;
	}
}
