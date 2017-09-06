package com.pancisin.bookster.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActivityType {
	FOLLOWING("FOLLOWING", "activity.type.following"), ATTENDING("ATTENDING", "activity.type.attending"), NOT_SPECIFIED(
			"FOLLOWING", "activity.type.following"), DELETE("DELETE", "activity.type.delete"), UPDATE("UPDATE",
					"activity.type.update"), CREATE_EVENT("CREATE_EVENT", "activity.type.create_event"), CREATE_SERVICE(
							"CREATE_SERVICE", "activity.type.create_service"), CREATE_ADMINISTRATOR("CREATE_ADMINISTRATOR",
									"activity.type.create_administrator"), CREATE_PLACE("CREATE_PLACE",
											"activity.type.create_place"), CREATE_ARTICLE("CREATE_ARTICLE",
													"activity.type.create_article"), CREATE_SURVEY("CREATE_SURVEY",
															"activity.type.create_survey");

	public final String name;
	public final String code;

	ActivityType(String name, String code) {
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
