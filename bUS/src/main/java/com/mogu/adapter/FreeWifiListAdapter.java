package com.mogu.adapter;

import java.util.List;

import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.entity.wifi.FreeWifiList;
import com.mogu.utils.WifiUtils;

import android.R.integer;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FreeWifiListAdapter extends BaseAdapter {
	private Context context;
	private List<FreeWifiList> freeWifiList;

	public FreeWifiListAdapter(Context context) {
		this.context = context;
	}

	public FreeWifiListAdapter(Context context, List<FreeWifiList> freeWifiList) {
		this.context = context;
		this.freeWifiList = freeWifiList;
	}

	@Override
	public int getCount() {
		return freeWifiList.size();
	}

	@Override
	public FreeWifiList getItem(int position) {
		return freeWifiList.get(position);
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
					R.layout.item_wifi, null);
			holder.ssd = (TextView) convertView.findViewById(R.id.ssd);
			holder.where = (TextView) convertView.findViewById(R.id.tv_where);
			holder.levelImageView = (ImageView) convertView
					.findViewById(R.id.iv_level);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ssd.setText(freeWifiList.get(position).getSsid00());
		if (WifiUtils.getEncryptWay(freeWifiList.get(position).scanResult) == 0) {
			holder.where.setText("无密码保护");
		} else {
			holder.where.setText("有密码保护");
		}

		int imageResourceId = getImageResourceId(freeWifiList.get(position).scanResult);
		holder.levelImageView.setImageResource(imageResourceId);
		return convertView;
	}

	private int getImageResourceId(ScanResult scanResult) {
		int imageResourceId = R.drawable.ewifi0;
		int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
		if (WifiUtils.getEncryptWay(scanResult) == 0) {
			imageResourceId = Constant.UNLOCK_LEVEL_IMAGE[level];
		} else {
			imageResourceId = Constant.LOCK_LEVEL_IMAGE[level];
		}
		return imageResourceId;
	}

	class ViewHolder {
		TextView ssd;
		TextView where;
		ImageView levelImageView;
	}
}
