package com.mogu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsMobile {
	public static boolean isMobileNO(String mobiles) {

		if (mobiles == null) {
			return false;
		}
		// Pattern p =
		// Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");

		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
