package com.mogu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.activity.SearchAllActivity;

/**
 * 公交
 * 
 * @author Administrator
 *
 */
public class ServerFragment extends Fragment {

	protected static final int SEARCH_START = 0;
	protected static final int SEARCH_END = 1;

	public List aaa = new ArrayList<String>();
	private View mLayout;
	private TextView searchTextView;

	private FragmentTabHost mTabHost;
	private View tabLayout;
	private TextView apolloText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_server, container,
					false);
			initView();
		}

		return mLayout;
	}

	private void initView() {
		searchTextView = (TextView) mLayout.findViewById(R.id.tv_search);
		searchTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchAllActivity.class);
				startActivity(intent);
			}
		});
		mTabHost = (FragmentTabHost) mLayout.findViewById(android.R.id.tabhost);
		// 初始化TabHost
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
		addTab(R.string.bus_text1, NearbyBusListFragment.class);
		addTab(R.string.bus_text2, BusListFragment.class);
		addTab(R.string.bus_text3, BusSearchFragment.class);
	}

	private void addTab(int strRes, Class class1) {
		tabLayout = getActivity().getLayoutInflater().inflate(
				R.layout.tab_bus_layout, null);
		apolloText = (TextView) tabLayout.findViewById(R.id.apollo_text);
		apolloText.setText(strRes);
		mTabHost.addTab(mTabHost.newTabSpec("" + strRes)
				.setIndicator(tabLayout), class1, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getChildFragmentManager().findFragmentByTag(""+R.string.bus_text3).onActivityResult(requestCode, resultCode, data);
	}

}
