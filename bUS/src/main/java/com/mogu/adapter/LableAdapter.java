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
import android.widget.TextView;

public class LableAdapter extends BaseAdapter {
	private Context context;
	private List<Child> list = new ArrayList<Child>();

	public LableAdapter(Context context, List<Child> list2) {
		this.context = context;
		this.list = list2;
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
					R.layout.item_lable, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.code = (TextView) convertView.findViewById(R.id.tv_code);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getIcname());
		holder.code.setText(list.get(position).getIcbh00());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView code;
	}
}
