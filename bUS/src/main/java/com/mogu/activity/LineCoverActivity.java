package com.mogu.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LineCoverActivity extends Activity{
	private TextView btn_ss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_line_cover);
	TextView title = (TextView) findViewById(R.id.title);
	title.setText("线路覆盖查询");
	ImageView pre = (ImageView) findViewById(R.id.pre);
	pre.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	});
	
	btn_ss = (TextView) findViewById(R.id.btn_ss);
	btn_ss.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Builder builder = new Builder(LineCoverActivity.this).setTitle("提示").setMessage("该线路所有车辆已覆盖设备").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder.show();
		}
	});
	
	}
}
