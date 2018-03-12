package com.mogu.adapter;

import java.util.ArrayList;

import com.mogu.activity.R;
import com.mogu.entity.mywifilist.WifiList1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyJudianAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<WifiList1> list = new ArrayList<WifiList1>();

	public MyJudianAdapter(Context context, ArrayList<WifiList1> list) {
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
					R.layout.item_my_judian, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.lv = (ImageView) convertView.findViewById(R.id.iv_lv);
			holder.jd = (ImageView) convertView.findViewById(R.id.img_jd);
			// holder.ssd = (TextView) convertView.findViewById(R.id.ssd);
			// holder.where = (TextView)
			// convertView.findViewById(R.id.tv_where);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getFenxmc());
		holder.time.setText(list.get(position).getFxsj00());
		if ("001".equals(list.get(position).getWifidj())) {
			holder.lv.setImageResource(R.drawable.icon_my_level_first);
			// holder.jd.setImageResource(R.drawable.ic_lv1);
		} else if ("002".equals(list.get(position).getWifidj())) {
			holder.lv.setImageResource(R.drawable.icon_my_level_second);
			// holder.jd.setImageResource(R.drawable.ic_lv2);
		} else {
			holder.lv.setImageResource(R.drawable.icon_my_level_third);
			// holder.jd.setImageResource(R.drawable.ic_lv3);
		}
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView time;
		ImageView lv;
		ImageView jd;
	}
}
