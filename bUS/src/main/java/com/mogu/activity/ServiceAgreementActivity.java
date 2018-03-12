package com.mogu.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServiceAgreementActivity extends BaseActivity implements
		OnClickListener {

	private TextView top_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_agreement);
		top_title = (TextView) findViewById(R.id.top_title);
		findViewById(R.id.btn_return).setOnClickListener(this);

		top_title.setText("用户服务协议");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_return:
			finish();
			break;

		default:
			break;
		}
	}

}
