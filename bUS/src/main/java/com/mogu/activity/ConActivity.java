package com.mogu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogu.fragment.ContactListFragment;

/**
 * 我的好友页面
 * 
 * @author Administrator
 *
 */
public class ConActivity extends FragmentActivity {
	private TextView btn_ss;
	private EditText nrong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_con);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我的好友");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		ContactListFragment contactListFragment = new ContactListFragment();
		transaction.add(R.id.ll_fragment, contactListFragment);
		transaction.commit();

	}
}
