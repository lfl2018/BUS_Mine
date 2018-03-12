package com.mogu.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BusDetailActivity extends Activity {
	
	private Vibrator mVibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busdetail);
		TextView title = (TextView) findViewById(R.id.tv_title);
		ImageView pre = (ImageView) findViewById(R.id.pre);
		title.setText("公交");
		mVibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pre.postDelayed(new Runnable() {

			@Override
			public void run() {
				AlertDialog dialog = new AlertDialog.Builder(
						BusDetailActivity.this).setTitle("提示")
						.setMessage("火车站已到，请下车！").setPositiveButton("确定", null)
						.create();
				dialog.show();
				mVibrator.vibrate(new long[]{1000,3000,1000,3000},-1);
			}
		}, 5000);
	}
}
