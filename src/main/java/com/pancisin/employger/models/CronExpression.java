package com.pancisin.employger.models;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CronExpression {

	private int[] minute;
	private int[] hour;
	@JsonProperty("day")
	private int[] dayOfMonth;
	private int[] month;
	private int[] dayOfWeek;

	private int[] weekOfMonth;

	public CronExpression() {

	}

	public CronExpression(String cron) {
		String parts[] = cron.split(" ");

		if (parts.length < 5)
			throw new IllegalArgumentException("Input has to be cron formatted.");
		
		this.setMinute(stringToIntArray(parts[0]));
		this.setHour(stringToIntArray(parts[1]));
		this.setDayOfMonth(stringToIntArray(parts[2]));
		this.setMonth(stringToIntArray(parts[3]));
		this.setDayOfWeek(stringToIntArray(parts[4]));
	}

	@Override
	public String toString() {
		String[] chars = new String[5];
		
		chars[0] = this.getMinute() == null ? "0" : IntArrayToString(this.getMinute());
		chars[1] = this.getHour() == null ? "0" : IntArrayToString(this.getHour());
		chars[2] = this.getDayOfMonth() == null ? "*" : IntArrayToString(this.getDayOfMonth());
		chars[3] = this.getMonth() == null ? "*" : IntArrayToString(this.getMonth());
		chars[4] = this.getDayOfWeek() == null ? "*" : IntArrayToString(this.getDayOfWeek());
		
		return String.join(" ", chars);
	}

	private String IntArrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i + 1 != array.length) {
				sb.append(",");
			}
		}

		return sb.toString();
	}

	private int[] stringToIntArray(String input) {
		if (input.equals("*") || input.equals("")) return null;
		return Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray();
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
