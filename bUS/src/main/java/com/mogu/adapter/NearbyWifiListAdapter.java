package com.mogu.adapter;

import java.util.ArrayList;

import com.mogu.activity.R;
import com.mogu.entity.nearwf.WifiList1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NearbyWifiListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<WifiList1>list = new ArrayList<WifiList1>();
	public NearbyWifiListAdapter(Context context, ArrayList<WifiList1> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_nearby_wifi, null);
			holder.ssd = (TextView) convertView.findViewById(R.id.ssd);
			holder.where = (TextView) convertView.findViewById(R.id.tv_where);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ssd.setText(list.get(position).getFenxmc());
		return convertView;
	}

	class ViewHolder {
		TextView ssd;
		TextView where;
	}
}
