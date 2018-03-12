package com.mogu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mogu.app.BusApp;

public class ScreenUtils {

	public interface OnSoftKeyBoardVisibleListener {
		void onSoftKeyBoardVisible(boolean visible);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int convertDpToPixel(Context context, float dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	protected static Boolean sLastVisiable = null;
	private static InputMethodManager manager;

	/** 监听软键盘状态 * @param activity * @param listener */
	public static void addOnSoftKeyBoardVisibleListener(Activity activity,
			final OnSoftKeyBoardVisibleListener listener) {
		final View decorView = activity.getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);
						int displayHight = rect.bottom - rect.top;
						int hight = decorView.getHeight();
						boolean visible = (double) displayHight / hight < 0.8;
						if (sLastVisiable == null || visible != sLastVisiable) {
							listener.onSoftKeyBoardVisible(visible);
						}
						sLastVisiable = visible;
					}
				});
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			View currentFocusView = activity.getCurrentFocus();
			if (currentFocusView != null)
				if (manager == null) {
					manager = (InputMethodManager) BusApp.getInstance()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
				}

			manager.hideSoftInputFromWindow(currentFocusView.getWindowToken(),
					InputMethodManager.RESULT_UNCHANGED_SHOWN);
		}
	}

	/**
	 * 打卡软键盘
	 * 
	 * @param mEditText
	 *            输入框
	 * @param mContext
	 *            上下文
	 */
	public static void openKeyboard(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

}
