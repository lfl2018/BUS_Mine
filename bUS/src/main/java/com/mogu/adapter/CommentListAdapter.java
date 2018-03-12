package com.mogu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import android.content.Context;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.entity.comment.Comment;
import com.mogu.entity.topiclist.HuatiList;
import com.mogu.utils.ImageUtils;

public class CommentListAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> list = new ArrayList<Comment>();

	public CommentListAdapter(Context context, List<Comment> list) {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_comment, null);
			holder.headImageView = (ImageView) convertView
					.findViewById(R.id.iv_head);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.replyView = (TextView) convertView
					.findViewById(R.id.tv_reply);
			holder.commentTextView = (TextView) convertView
					.findViewById(R.id.tv_context);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Comment comment = list.get(position);
		x.image().bind(holder.headImageView, comment.getTxiang());
		holder.nameTextView.setText(comment.getHyming());
		String context = comment.getNrong0();
		if (context != null) {
			if (context.startsWith(Constant.REPLY_MARK)) {
				holder.replyView.setVisibility(View.VISIBLE);
				holder.commentTextView.setText(context.replace(
						Constant.REPLY_MARK, ""));
			} else {
				holder.replyView.setVisibility(View.GONE);
				holder.commentTextView.setText(context);
			}
		}
		holder.timeTextView.setText(comment.getFbriqi());
		return convertView;
	}

	private class ViewHolder {
		ImageView headImageView;
		TextView nameTextView;
		View replyView;
		TextView commentTextView;
		TextView timeTextView;

	}
}
