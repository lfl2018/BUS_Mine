package com.mogu.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.ChengChejlActivity;
import com.mogu.activity.NewsDetailsActivity;
import com.mogu.activity.R;
import com.mogu.adapter.NewsAdapter;

public class MyNewsFragment extends Fragment {

	private View mLayout;
	private ListView lv_news;
	private NewsAdapter adapter;

	public MyNewsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_mynews, container,
					false);
			initView(mLayout);
			
		}

		return mLayout;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		lv_news = (ListView) view.findViewById(R.id.lv_news);
		adapter = new NewsAdapter(getActivity());
		lv_news.setAdapter(adapter);
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),NewsDetailsActivity.class));
			}
		});
	}

	
}
