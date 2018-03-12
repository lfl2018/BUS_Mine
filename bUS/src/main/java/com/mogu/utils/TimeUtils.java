package com.mogu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static String getCurrentTime() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return time;
	}
	
	public static String getCurrentTime(String formatString) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat(formatString);
		String time = format.format(date);
		return time;
	}
}
