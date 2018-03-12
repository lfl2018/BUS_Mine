package com.mogu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.activity.UserDetailInfoActivity;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.wfuser.FriendsList;
import com.mogu.utils.ImageUtils;

public class WifiUserListAdapter extends BaseAdapter {
	private List<FriendsList> list = new ArrayList<FriendsList>();
	private Context context;

	public WifiUserListAdapter(Context context, List<FriendsList> list) {
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
					R.layout.item_chat_all, null);

			holder.self = (TextView) convertView.findViewById(R.id.tv_self);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.sign = (TextView) convertView.findViewById(R.id.tv_sign);
			holder.head = (ImageView) convertView.findViewById(R.id.img_head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int index = position;
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(index).getHybh00()
						.equals(MyCookieStore.user.getHybh00())) {
					return;
				}
				Intent intent = new Intent(context,
						UserDetailInfoActivity.class);
				intent.putExtra(Constant.FRIEND_INFO, list.get(index));
				intent.putExtra("flag", list.get(index).getFlag00());
				context.startActivity(intent);
			}
		});
		holder.name.setText(list.get(position).getHyming());
		holder.sign.setText(list.get(position).getGxqm00());
		x.image().bind(holder.head,
				ImageUtils.getImageUrl(list.get(position).getTxiang()));
		if (list.get(position).getHybh00()
				.equals(MyCookieStore.user.getHybh00())) {
			holder.self.setVisibility(View.VISIBLE);
		} else {
			holder.self.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView self;
		public TextView name;
		public TextView sign;
		public ImageView head;
	}
}
