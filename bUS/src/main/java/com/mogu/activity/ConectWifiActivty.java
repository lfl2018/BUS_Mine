package com.mogu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ConectWifiActivty extends Activity{
	private EditText Epass;
	private EditText Ename;
	private TextView Tmistake;
	private CheckBox Cshare;
	private Button Bconect;
	
	private String Strname;
	private String Strpass;
	private String StrSSID;
	private String StrBSSID;
	private String Strlat;
	private String Strlgt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conect_wifi);
		initViews();
	}

	private void initViews() {
		
		Epass = (EditText) findViewById(R.id.e_pass);
		Ename = (EditText) findViewById(R.id.e_name);
		Cshare = (CheckBox) findViewById(R.id.c_share);
		Bconect = (Button) findViewById(R.id.b_conect);
		Tmistake= (TextView) findViewById(R.id.t_mistake);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
