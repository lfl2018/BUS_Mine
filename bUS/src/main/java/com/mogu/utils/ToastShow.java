package com.mogu.utils;

import com.mogu.app.BusApp;

import android.content.Context;
import android.widget.Toast;

public class ToastShow {
	private static Toast toast;
	private static Context context;
	/**
	 * 测试专用，发布时请修改成false。 默认值为true，true为显示Toast，false为不显示Toast。
	 */
	private static boolean isShow = true;

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		ToastShow.context = context;
	}

	public static void shortShowToast(String text) {
		if (context == null) {
			context = BusApp.getInstance();
			if (context == null) {
				return;
			}
		}
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			if (toast.getDuration() != Toast.LENGTH_SHORT) {
				toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.setText(text);
		}
		toast.show();
	}

	/**
	 * 测试专用，发布时请修改isShow成false。{@link #isShow}
	 */
	public static void testShortShowToast(String text) {
		if (!isShow) {
			return;
		}
		if (context == null) {
			context = BusApp.getInstance();
			if (context == null) {
				return;
			}
		}
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			if (toast.getDuration() != Toast.LENGTH_SHORT) {
				toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.setText(text);
		}
		toast.show();
	}

	public static void longShowToast(String text) {
		if (context == null) {
			context = BusApp.getInstance();
			if (context == null) {
				return;
			}
		}
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		} else {
			if (toast.getDuration() != Toast.LENGTH_LONG) {
				toast.setDuration(Toast.LENGTH_LONG);
			}
			toast.setText(text);
		}
		toast.show();
	}
}