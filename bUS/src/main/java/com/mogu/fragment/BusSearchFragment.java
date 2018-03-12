package com.mogu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.BusListActivity;
import com.mogu.activity.R;
import com.mogu.activity.SearchActivity;
import com.mogu.activity.SearchAllActivity;

/**
 * 公交
 * 
 * @author Administrator
 *
 */
public class BusSearchFragment extends Fragment {

	protected static final int SEARCH_START = 0;
	protected static final int SEARCH_END = 1;

	public List aaa = new ArrayList<String>();
	private View mLayout;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView listview;
	private adapter adapter;

	private TextView startTextView;
	private TextView endTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_bus_search, container,
					false);
			initView();
		}

		// mSwipeRefreshLayout = (SwipeRefreshLayout) layout
		// .findViewById(R.id.refresh);
		//
		// listview = (ListView) layout.findViewById(R.id.listView1);
		// adapter = new adapter();
		// listview.setAdapter(adapter);
		// mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
		// public void onRefresh() {
		// layout.postDelayed(new Runnable() {
		//
		// public void run() {
		// aaa.add("title+");
		// adapter.notifyDataSetChanged();
		// mSwipeRefreshLayout.setRefreshing(false);
		// }
		// }, 2000);
		// }
		// });

		return mLayout;
	}

	private void initView() {
		startTextView = (TextView) mLayout.findViewById(R.id.tv_input_start);
		endTextView = (TextView) mLayout.findViewById(R.id.tv_input_end);
		startTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchActivity.class);
				startActivityForResult(intent, SEARCH_START);
			}
		});
		endTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchActivity.class);
				startActivityForResult(intent, SEARCH_END);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == SEARCH_START) {
			startTextView.setText("我的位置");
		} else if (requestCode == SEARCH_END) {
			endTextView.setText("蔡塘");
		}
		if ((!startTextView.getText().toString().equals("输入起点"))
				&& (!endTextView.getText().toString().equals("输入终点"))) {
			startActivity(new Intent(getActivity(), BusListActivity.class));
		}
	}

	private void initData() {
		for (int i = 1; i < 11; i++) {
			aaa.add("title" + i);
		}
	}

	class adapter extends BaseAdapter {

		public int getCount() {
			return aaa.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = convertView.inflate(getActivity(),
					R.layout.discover_item, null);
			TextView titile = (TextView) inflate.findViewById(R.id.title);
			return inflate;
		}
	}
}
