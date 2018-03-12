package com.mogu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import com.mogu.activity.R;
import com.mogu.app.Url;
import com.mogu.entity.lable.Child;
import com.mogu.entity.mission.MrrwList;
import com.mogu.entity.mychengjiu.CjList;
import com.mogu.entity.nearwf.WifiList1;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyLabelListAdapter extends BaseAdapter {
	private Context context;
	private List<String> list = new ArrayList<String>();
	// 用来控制选中状况
	private SparseBooleanArray isSelected;

	public SparseBooleanArray getIsSelected() {
		return isSelected;
	}

	public MyLabelListAdapter(Context context, List<String> list2) {
		this.context = context;
		this.list = list2;
		isSelected = new SparseBooleanArray();
		initDate();
	}

	// 初始化isSelected的数据
	public void initDate() {
		for (int i = 0; i < list.size(); i++) {
			isSelected.put(i, true);
		}
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_select_label, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_lable);
			holder.ivSelect = (ImageView) convertView
					.findViewById(R.id.iv_select);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int index = position;
		holder.tvName.setText(list.get(position));

		// 监听checkBox并根据原来的状态来设置新的状态
		holder.tvName.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (isSelected.get(index)) {
					isSelected.put(index, false);
				} else {
					isSelected.put(index, true);
				}
				notifyDataSetChanged();
			}
		});
		holder.ivSelect.setVisibility(isSelected.get(position) ? View.VISIBLE
				: View.GONE);

		return convertView;
	}

	static class ViewHolder {
		TextView tvName;
		ImageView ivSelect;
	}
}
