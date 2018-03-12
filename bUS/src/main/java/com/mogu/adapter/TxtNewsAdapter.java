package com.mogu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.entity.topiclist.HuatiList;
import com.mogu.entity.txtnews.NewsList;
import com.mogu.utils.ImageUtils;
/**
 * 文字新闻适配器
 * @author Administrator
 *
 */
public class TxtNewsAdapter extends BaseAdapter {
	private Context context;
	private List<NewsList>list = new ArrayList<NewsList>();
	public TxtNewsAdapter(Context context, List<NewsList>list) {
		this.list = list;
		this.context = context;
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
		ViewHolder holder;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_txtnews, null);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.good = (TextView) convertView.findViewById(R.id.tv_good);
			holder.comment = (TextView) convertView.findViewById(R.id.tv_comment);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(list.get(position).getFbriqi());
		holder.title.setText(list.get(position).getBiaoti());
//		if (list.get(position).getHzshu0() == null) {
//			holder.good.setText("获赞 0");
//		}else {
//			holder.good.setText("获赞 "+list.get(position).getHzshu0());
//		}
//		if (list.get(position).getPlshu0() == null) {
//			holder.comment.setText("评论 0");
//		}else {
//			holder.comment.setText("评论 "+list.get(position).getPlshu0());
//		}
		return convertView;
	}
	private class ViewHolder{
		TextView title;
		TextView time;
		TextView good;
		TextView comment;
		
	}
}
