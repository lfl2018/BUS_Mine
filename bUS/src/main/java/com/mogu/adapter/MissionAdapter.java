package com.mogu.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import com.mogu.activity.R;
import com.mogu.app.Url;
import com.mogu.entity.mission.MrrwList;
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

public class MissionAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MrrwList> list = new ArrayList<MrrwList>();

	public MissionAdapter(Context context, ArrayList<MrrwList> list) {
		this.context = context;
		this.list = list;
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
					R.layout.item_mission, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.finish = (TextView) convertView.findViewById(R.id.tv_finish);
			holder.unfinish = (TextView) convertView
					.findViewById(R.id.tv_un_finish);
			holder.getjf = (TextView) convertView.findViewById(R.id.tv_get_jf);
			holder.fenshu = (TextView) convertView.findViewById(R.id.tv_fenshu);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getMrrwmc());
		holder.fenshu.setText("+"+list.get(position).getJljfsl()+" 积分");
		final int index = position;
		if (list.get(index).getMrrwzt().equals("001")) {
			//未领取积分
			holder.finish.setVisibility(View.GONE);
			holder.unfinish.setVisibility(View.GONE);
			holder.getjf.setVisibility(View.VISIBLE);
		} else if (list.get(index).getMrrwzt().equals("002")) {
			//已领取积分
			holder.finish.setVisibility(View.VISIBLE);
			holder.unfinish.setVisibility(View.GONE);
			holder.getjf.setVisibility(View.GONE);
		} else {
			//未完成任务
			holder.finish.setVisibility(View.GONE);
			holder.unfinish.setVisibility(View.VISIBLE);
			holder.getjf.setVisibility(View.GONE);
		}
		holder.getjf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SessionRequestParams params = new SessionRequestParams(Url.Mission_jl);
				params.addBodyParameter("mrrwbh", list.get(index).getMrrwbh());
				x.http().post(params, new HttpCallBack() {
					
					@Override
					public void onFinished() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(Throwable arg0, boolean arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCancelled(CancelledException arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void success(String result) throws JSONException {
						JSONObject json = new JSONObject(result);
						if (json.getString("code").equals("1")) {
							ToastShow.shortShowToast("获取成功");
							holder.finish.setVisibility(View.VISIBLE);
							holder.unfinish.setVisibility(View.GONE);
							holder.getjf.setVisibility(View.GONE);
						}else {
							ToastShow.shortShowToast(json.getString("msg"));
						}
					}
				});
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView fenshu;
		TextView finish;
		TextView unfinish;
		TextView getjf;
	}
}
