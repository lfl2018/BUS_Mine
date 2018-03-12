package com.mogu.adapter;

import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyLableAdapter extends BaseAdapter {
	private Context context;
	private List<String> list = new ArrayList<String>();
	private boolean wantchange;

	public MyLableAdapter(Context context, List<String> list2,
			boolean wantchange) {
		this.context = context;
		this.list = list2;
		this.wantchange = wantchange;
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
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_my_lable, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_lable);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position));
		final int index = position;
		return convertView;
	}

	static class ViewHolder {
		TextView name;
	}
}
