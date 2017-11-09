package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PageRole {
	ROLE_OWNER("ROLE_OWNER", "role.owner", 100),
	ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR", "role.administrator", 60),
	ROLE_MODERATOR("ROLE_MODERATOR", "role.moderator", 40);

	PageRole(String name, String code, int level) {
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
