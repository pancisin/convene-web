package com.pancisin.bookster.models.enums;

public enum Role {
	ROLE_OWNER("role.owner"), ROLE_ADMINISTRATOR("role.administrator");
	
	Role(String code) {
		this.code = code;
	}
	
	private final String code;
	
	public String getCode() {
		return code;
	}
}
