package com.pancisin.employger.repository.converters;

import javax.persistence.AttributeConverter;

import com.pancisin.employger.models.Schedule;

public class ScheduleConverter implements AttributeConverter<Schedule, String> {

	@Override
	public String convertToDatabaseColumn(Schedule arg0) {
		return arg0.toString();
	}

	@Override
	public Schedule convertToEntityAttribute(String arg0) {
		return new Schedule(arg0);
	}
}
