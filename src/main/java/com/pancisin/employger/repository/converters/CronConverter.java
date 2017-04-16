package com.pancisin.employger.repository.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

import com.pancisin.employger.models.CronExpression;

public class CronConverter implements AttributeConverter<CronExpression, String> {
	private static final int[] EMPTY = {};
	private final String[] weekDays = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
	
	@Override
	public String convertToDatabaseColumn(CronExpression arg0) {
		String[] chars = new String[6];

		chars[0] = arg0.getSecond() == null ? "0" : IntArrayToString(arg0.getSecond());
		chars[1] = arg0.getMinute() == null ? "0" : IntArrayToString(arg0.getMinute());
		chars[2] = arg0.getHour() == null ? "0" : IntArrayToString(arg0.getHour());
		chars[3] = arg0.getDayOfMonth() == null || arg0.getDayOfMonth().length == 0  ? "?" : IntArrayToString(arg0.getDayOfMonth());
		chars[4] = arg0.getMonth() == null || arg0.getMonth().length == 0 ? "*" : IntArrayToString(arg0.getMonth());
		chars[5] = arg0.getDayOfWeek() == null || arg0.getDayOfWeek().length == 0 ? "*" : intToWeekdayString(arg0.getDayOfWeek());
		chars[5] += arg0.getWeekOfMonth() != null && arg0.getWeekOfMonth().length != 0 ? "#" + IntArrayToString(arg0.getWeekOfMonth()) : "";
		
		return String.join(" ", chars);
	}
	
	private String intToWeekdayString(int[] array) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0) {
				sb.append(weekDays[array[i] - 1]);
				
				if (i + 1 != array.length) {
					sb.append(",");
				}
			}
		}
		
		return sb.toString();
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

	@Override
	public CronExpression convertToEntityAttribute(String arg0) {
		CronExpression cron = new CronExpression();

		String parts[] = arg0.split(" ");

		if (parts.length < 6) {
			throw new IllegalArgumentException("Input has to be cron formatted.");
		}

		int second[] = stringToIntArray(parts[0]);
		cron.setSecond(second.length == 0 ? cron.getSecond() : second);
		
		int minutes[] = stringToIntArray(parts[1]);
		cron.setMinute(minutes.length == 0 ? cron.getMinute() : minutes);

		int[] hours = stringToIntArray(parts[2]);
		cron.setHour(hours.length == 0 ? cron.getHour() : hours);
		cron.setDayOfMonth(stringToIntArray(parts[3]));
		cron.setMonth(stringToIntArray(parts[4]));
		
		String[] week_parts = parts[5].split("#");
		if (week_parts.length > 1) {
			cron.setWeekOfMonth(stringToIntArray(week_parts[1]));
		}
		cron.setDayOfWeek(weekDayStringToIntArray(week_parts[0]));
		return cron;
	}

	private int[] stringToIntArray(String input) {
		if (input.equals("*") || input.equals("") || input.equals("?"))
			return EMPTY;
		return Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray();
	}
	
	private int[] weekDayStringToIntArray(String input) {
		if (input.equals("*") || input.equals("") || input.equals("?"))
			return EMPTY;
		
		List<Integer> result = new ArrayList<Integer>();
		
//		int[] result = new innt[7];
		String[] days = input.split(",");
		
		for (int i = 0; i < days.length; i++) {
			int index = -1;
			for (int j = 0; j < weekDays.length; j++) {
				if (weekDays[j].equals(days[i])) {
					index = j;
					break;
				}
			}
			
			result.add(index + 1);
		}
		
		int[] intArray = new int[result.size()];
		for (int i = 0; i < result.size(); i++) {
			intArray[i] = result.get(i);
		}
		
		return intArray;
	}
}
