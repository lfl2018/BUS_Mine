package com.mogu.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.adapter.NewsAdapter;
import com.mogu.fragment.XiaZaiZhongFragment;
import com.mogu.fragment.YiXiaZaiFragment;

public class MyDownloadActivity extends FragmentActivity implements OnClickListener {
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private View mLayout;
	private ListView lv_news;
	private NewsAdapter adapter;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;

	private RadioGroup rGroup;
	private Fragment f1, f2;

	public MyDownloadActivity() {

	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		if (mLayout == null) {
//			mLayout = inflater.inflate(R.layout.fragment_news, container,
//					false);
//			manager = getSupportFragmentManager();
//			transaction = manager.beginTransaction();
//			initView(mLayout);
//		}
//
//		return mLayout;
//	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mydownload);
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("下载记录");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		rb1 = (RadioButton) findViewById(R.id.rb_1);
		rb2 = (RadioButton) findViewById(R.id.rb_2);

		rGroup = (RadioGroup) findViewById(R.id.rg_news);

		/**
		 * 为三个按钮添加监听
		 */
		rb1.setOnClickListener(this);
		rb2.setOnClickListener(this);

		rGroup.check(R.id.rb_1);
		f1 = new YiXiaZaiFragment();
		transaction.replace(R.id.fra_news, f1);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();

		switch (v.getId()) {
		case R.id.rb_1:
			/**
			 * 为了防止重叠，需要点击之前先移除其他Fragment
			 */
			hideFragment(transaction);
			f1 = new YiXiaZaiFragment();
			transaction.replace(R.id.fra_news, f1);
			transaction.commit();

			break;
		case R.id.rb_2:
			hideFragment(transaction);
			f2 = new XiaZaiZhongFragment();
			transaction.replace(R.id.fra_news, f2);
			transaction.commit();

			break;

		default:
			break;
		}
	}

	/*
	 * 去除（隐藏）所有的Fragment
	 */
	private void hideFragment(FragmentTransaction transaction) {
		if (f1 != null) {
			// transaction.hide(f1);隐藏方法也可以实现同样的效果，不过我一般使用去除
			transaction.remove(f1);
		}
		if (f2 != null) {
			// transaction.hide(f2);
			transaction.remove(f2);
		}
	}

}
