package com.mogu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogu.app.Constant;
import com.mogu.utils.ToastShow;

public class MyLabelActivity extends Activity implements OnClickListener {
	private EditText etLabel;
	private String strLabel;
	private TextView tvChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_label);
		initViews();
	}

	private void initViews() {
		etLabel = (EditText) findViewById(R.id.et_label);
		tvChange = (TextView) findViewById(R.id.right);
		tvChange.setText("保存");
		tvChange.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("添加标签");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right:
			strLabel = etLabel.getText().toString().trim();
			if (TextUtils.isEmpty(strLabel)) {
				ToastShow.shortShowToast("请输入标签！");
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(Constant.LABEL_VALUE, strLabel);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.pre:
			finish();
			break;

		default:
			break;
		}
	}

}
