package com.mogu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mogu.activity.R;
import com.mogu.adapter.NewsAdapter;

public class NewsFragment extends Fragment implements OnClickListener {
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

	public NewsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_news, container,
					false);
			manager = getActivity().getSupportFragmentManager();
			transaction = manager.beginTransaction();
			initView(mLayout);
		}

		return mLayout;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		rb1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb2 = (RadioButton) view.findViewById(R.id.rb_2);

		rGroup = (RadioGroup) view.findViewById(R.id.rg_news);

		/**
		 * 为三个按钮添加监听
		 */
		rb1.setOnClickListener(this);
		rb2.setOnClickListener(this);

		rGroup.check(R.id.rb_1);
		f1 = new MyNewsFragment();
		transaction.replace(R.id.fra_news, f1);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		manager = getActivity().getSupportFragmentManager();
		transaction = manager.beginTransaction();

		switch (v.getId()) {
		case R.id.rb_1:
			/**
			 * 为了防止重叠，需要点击之前先移除其他Fragment
			 */
			hideFragment(transaction);
			f1 = new MyNewsFragment();
			transaction.replace(R.id.fra_news, f1);
			transaction.commit();

			break;
		case R.id.rb_2:
			hideFragment(transaction);
			f2 = new VideoFragment();
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
