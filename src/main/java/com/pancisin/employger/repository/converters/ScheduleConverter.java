package com.pancisin.employger.repository.converters;

import javax.persistence.AttributeConverter;

import com.pancisin.employger.models.CronExpression;

public class ScheduleConverter implements AttributeConverter<CronExpression, String> {

	@Override
	public String convertToDatabaseColumn(CronExpression arg0) {
		return arg0.toString();
	}

	@Override
	public CronExpression convertToEntityAttribute(String arg0) {
		return new CronExpression(arg0);
	}
}
