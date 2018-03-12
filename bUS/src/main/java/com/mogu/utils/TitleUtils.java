package com.mogu.utils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mogu.activity.R;

public class TitleUtils {

	public static void setTitle(final Activity activity, int topBarResID,
			String titleName) {
		setTitle(activity,activity.findViewById(topBarResID),titleName);
	}
	
	public static void setTitle(final Activity activity, View topBarView,
			String titleName) {

		topBarView.findViewById(R.id.pre).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						activity.finish();
					}
				});

		TextView titleView = (TextView) topBarView.findViewById(R.id.title);
		titleView.setText(titleName);
	}

	public static void setTitle(final Activity activity, int topBarResID,
			String titleName, boolean isShowLeft) {
		setTitle(activity,activity.findViewById(topBarResID),titleName,isShowLeft);
	}
	
	public static void setTitle(final Activity activity, View topBarView,
			String titleName, boolean isShowLeft) {

		topBarView.findViewById(R.id.pre).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						activity.finish();
					}
				});
		topBarView.findViewById(R.id.pre).setVisibility(
				isShowLeft ? View.VISIBLE : View.INVISIBLE);

		TextView titleView = (TextView) topBarView.findViewById(R.id.title);
		titleView.setText(titleName);
	}

	public static void setTitle(final Activity activity, int topBarResID,
			String titleName, boolean isShowLeft, boolean isShowRight) {
		setTitle(activity,activity.findViewById(topBarResID),titleName,isShowLeft,isShowRight);
	}
	
	public static void setTitle(final Activity activity, View topBarView,
			String titleName, boolean isShowLeft, boolean isShowRight) {

		topBarView.findViewById(R.id.pre).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						activity.finish();
					}
				});
		topBarView.findViewById(R.id.pre).setVisibility(
				isShowLeft ? View.VISIBLE : View.INVISIBLE);
//		topBarView.findViewById(R.id.topbar_right).setVisibility(
//				isShowRight ? View.VISIBLE : View.INVISIBLE);

		TextView titleView = (TextView) topBarView.findViewById(R.id.title);
		titleView.setText(titleName);
	}

}
