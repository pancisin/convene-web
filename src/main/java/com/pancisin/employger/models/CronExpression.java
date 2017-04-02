package com.pancisin.employger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pancisin.employger.repository.converters.CronConverter;

public class CronExpression {

	private int[] minute = { 0 };
	private int[] hour = { 0 };
	@JsonProperty("day")
	private int[] dayOfMonth = {};
	private int[] month = {};
	private int[] dayOfWeek = {};

	private int[] weekOfMonth = {};

	@Override
	public String toString() {
		CronConverter conv = new CronConverter();
		return conv.convertToDatabaseColumn(this);
	}
	
	public int[] getMinute() {
		return minute;
	}

	public void setMinute(int[] minute) {
		this.minute = minute;
	}

	public int[] getHour() {
		return hour;
	}

	public void setHour(int[] hour) {
		this.hour = hour;
	}

	public int[] getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int[] dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int[] getMonth() {
		return month;
	}

	public void setMonth(int[] month) {
		this.month = month;
	}

	public int[] getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int[] dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int[] getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(int[] weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}
}
