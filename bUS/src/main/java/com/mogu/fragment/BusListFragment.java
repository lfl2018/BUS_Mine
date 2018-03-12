package com.mogu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mogu.activity.BusDetailActivity;
import com.mogu.activity.R;

public class BusListFragment extends Fragment {

	private ListView listview;
	private MyAdapter adapter;
	private View mLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_bus_list, container,
					false);
			initView();
		}
		return mLayout;
	}

	private void initView() {
		listview = (ListView) mLayout.findViewById(R.id.listView1);
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						BusDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return 6;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = convertView.inflate(getActivity(),
					R.layout.item_bus_recently, null);
			return inflate;
		}
	}
}
