package com.mogu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyAllWiFiActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_all_wifi);
		TextView title = (TextView) findViewById(R.id.tv_title);
		ImageView pre = (ImageView) findViewById(R.id.pre);
		title.setText("附近热点");
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
