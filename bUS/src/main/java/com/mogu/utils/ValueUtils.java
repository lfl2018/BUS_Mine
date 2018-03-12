package com.mogu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.text.TextUtils;

public class ValueUtils {

	private static int length = 3;

	public static String getCode(int position) {
		position++;
		String code = position + "";
		for (int i = code.length(); i < length; i++) {
			code = "0" + code;
		}
		return code;
	}

	/**
	 * 获取环信id
	 * 
	 * @param position
	 * @return
	 */
	public static String getHXId(String id) {
		String code = "";
		if (id == null) {
			return System.currentTimeMillis() + "";
		}
		code = id.toLowerCase(Locale.US);
		return code;
	}

	public static String getString(List<String> list) {

		return getString(list, ";");
	}

	public static String getString(List<String> list, String suffix) {
		String mString = "";
		for (String string : list) {
			mString = mString + string + suffix;
		}
		if (mString.endsWith(suffix)) {
			mString = mString.substring(0, mString.length() - suffix.length());
		}
		return mString;
	}

	public static String getValue(String string) {
		if (string == null||"NULL".equalsIgnoreCase(string)) {
			string = "";
		}
		return string;
	}

	public static String getValue(String string, String defaultString) {
		if (TextUtils.isEmpty(string)) {
			string = defaultString;
		}
		return string;
	}

	public static String getValue(Integer integer) {
		String string;
		if (integer == null) {
			string = "0";
		} else {
			string = integer.toString();
		}
		return string;
	}

	public static String getValue(Integer integer, int defaultInt) {
		String string;
		if (integer == null) {
			string = defaultInt + "";
		} else {
			string = integer.toString();
		}
		return string;
	}

	public static List<String> getList(String string, String suffix) {

		List<String> imageUrlList = new ArrayList<String>();

		if (string == null) {
			return imageUrlList;
		}
		String[] imageUrls = string.split(suffix);
		for (String str : imageUrls) {
			imageUrlList.add(str);
		}

		return imageUrlList;
	}

}
