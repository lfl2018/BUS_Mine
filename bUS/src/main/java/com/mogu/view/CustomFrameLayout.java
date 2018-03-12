package com.mogu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 触摸事件不传递给子控件的FrameLayout控件,
 * 重写了onInterceptTouchEvent方法
 * 
 * @author Administrator
 *
 */
public class CustomFrameLayout extends FrameLayout {

	@SuppressLint("NewApi")
	public CustomFrameLayout(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public CustomFrameLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CustomFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomFrameLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;// 让触摸事件不传递
	}

}
