package com.mogu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BusListActivity extends Activity {

	private ListView listview;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_list);
		TextView title = (TextView) findViewById(R.id.title);
		ImageView pre = (ImageView) findViewById(R.id.pre);
		title.setText("线路");
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview = (ListView) findViewById(R.id.listView1);
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BusListActivity.this,
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
			View inflate = convertView.inflate(BusListActivity.this,
					R.layout.item_bus, null);
			return inflate;
		}
	}
}
