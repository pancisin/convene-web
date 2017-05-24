package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {
	ROLE_OWNER("ROLE_OWNER", "role.owner", 80), ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR", "role.administrator",
			60), ROLE_REPORTER("ROLE_REPORTER", "role.reporter", 40), ROLE_VISITOR("ROLE_VISITOR", "role.visitor", 20);

	Role(String name, String code, int level) {
		this.name = name;
		this.code = code;
		this.level = level;
	}

	private final String code;
	private final String name;
	private final int level;

	@JsonView(Compact.class)
	public String getCode() {
		return code;
	}

	@JsonView(Compact.class)
	public String getName() {
		return name;
	}

	@JsonView(Compact.class)
	public int getLevel() {
		return level;
	}
}
