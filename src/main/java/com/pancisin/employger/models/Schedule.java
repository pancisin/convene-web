package com.pancisin.employger.models;

import java.util.stream.Stream;

public class Schedule {

	private int[] dw;
	private int[] wm;
	private int[] m;
	private int[] hour;
	private int[] minute;

	public Schedule() {

	}

	public Schedule(String cron) {
		String parts[] = cron.split(" ");

		if (parts.length < 5)
			throw new IllegalArgumentException("Input has to be cron formatted.");
		
		this.setMinute(stringToIntArray(parts[0]));
		this.setHour(stringToIntArray(parts[1]));
		this.setDw(stringToIntArray(parts[2]));
		this.setWm(stringToIntArray(parts[3]));
		this.setM(stringToIntArray(parts[4]));
	}

	@Override
	public String toString() {
		String[] chars = new String[5];
		
		chars[0] = this.getMinute() == null ? "*" : IntArrayToString(this.getMinute());
		chars[1] = this.getHour() == null ? "*" : IntArrayToString(this.getHour());
		chars[2] = this.getDw() == null ? "*" : IntArrayToString(this.getDw());
		chars[3] = this.getWm() == null ? "*" : IntArrayToString(this.getWm());
		chars[4] = this.getM() == null ? "*" : IntArrayToString(this.getM());
		
		return String.join(" ", chars);
	}

	private String IntArrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i + 1 != array.length) {
				sb.append("/");
			}
		}

		return sb.toString();
	}

	private int[] stringToIntArray(String input) {
		if (input.equals("*") || input.equals("")) return null;
		return Stream.of(input.split("/")).mapToInt(Integer::parseInt).toArray();
	}

	public int[] getDw() {
		return dw;
	}

	public void setDw(int[] dw) {
		this.dw = dw;
	}

	public int[] getWm() {
		return wm;
	}

	public void setWm(int[] wm) {
		this.wm = wm;
	}

	public int[] getM() {
		return m;
	}

	public void setM(int[] m) {
		this.m = m;
	}

	public int[] getHour() {
		return hour;
	}

	public void setHour(int[] hour) {
		this.hour = hour;
	}

	public int[] getMinute() {
		return minute;
	}

	public void setMinute(int[] minute) {
		this.minute = minute;
	}
}
