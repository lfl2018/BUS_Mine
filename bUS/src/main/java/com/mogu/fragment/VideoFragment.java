package com.mogu.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.adapter.NewsAdapter;
import com.mogu.adapter.VideoAdapter;

public class VideoFragment extends Fragment {

	private View mLayout;
	private ListView lv_news;
	private VideoAdapter adapter;

	public VideoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_video, container,
					false);
			initView(mLayout);
			
		}

		return mLayout;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		lv_news = (ListView) view.findViewById(R.id.lv_video);
		adapter = new VideoAdapter(getActivity());
		lv_news.setAdapter(adapter);
	}

	
}
