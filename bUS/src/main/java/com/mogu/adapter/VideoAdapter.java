package com.mogu.adapter;

import com.mogu.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter{
	private Context context;
	public VideoAdapter(Context context){
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_video, null);
//			holder.ssd = (TextView) convertView.findViewById(R.id.ssd);
//			holder.where = (TextView) convertView.findViewById(R.id.tv_where);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	class ViewHolder{
		TextView ssd;
		TextView where;
	}
}
