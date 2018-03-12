package com.mogu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.entity.mywifilist.WifiList2;

public class OtherJudianAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<WifiList2> list = new ArrayList<WifiList2>();

	public OtherJudianAdapter(Context context, ArrayList<WifiList2> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_other_judian, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			// holder.ssd = (TextView) convertView.findViewById(R.id.ssd);
			// holder.where = (TextView)
			// convertView.findViewById(R.id.tv_where);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getFenxmc());
		holder.time.setText(list.get(position).getFxsj00());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView time;
	}
}
