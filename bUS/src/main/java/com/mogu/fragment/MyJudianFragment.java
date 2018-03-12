package com.mogu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.JuDianDetailActivity;
import com.mogu.activity.R;
import com.mogu.adapter.MyJudianAdapter;
import com.mogu.app.Constant;
import com.mogu.app.Url;
import com.mogu.entity.mywifilist.MyWifiList;
import com.mogu.entity.mywifilist.WifiList1;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.SessionRequestParams;

public class MyJudianFragment extends Fragment {
	private View mLayout;
	private MyJudianAdapter adapter;
	private ListView lv_my_jd;
	private ArrayList<WifiList1> list = new ArrayList<WifiList1>();
	private TextView notice;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_my_judian, null);
			initViews(mLayout);
		}
		return mLayout;
	}

	private void initViews(View view) {
		notice = (TextView) view.findViewById(R.id.tv_notice);
		lv_my_jd = (ListView) view.findViewById(R.id.lv_my_jd);
		adapter = new MyJudianAdapter(getActivity(), list);
		lv_my_jd.setAdapter(adapter);
		lv_my_jd.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), JuDianDetailActivity.class);
				intent.putExtra("LV", list.get(position).getWifidj());
				intent.putExtra("wifibh", list.get(position).getWifibh());
				intent.putExtra(Constant.JUDIAN_NAME, list.get(position).getFenxmc());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getdata();
	}

	private void getdata() {
		SessionRequestParams params = new SessionRequestParams(
				Url.MyShareWifi_URL);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				MyWifiList json = GsonUtils.parseJSON(arg0, MyWifiList.class);
				if (json.getCode().equals("1")) {
					List<WifiList1> mlist = new ArrayList<WifiList1>();
					mlist = json.getWifiList1();
					list.clear();
					list.addAll(mlist);
					if (list.size() == 0) {
						lv_my_jd.setVisibility(View.GONE);
						notice.setVisibility(View.VISIBLE);
					} else {
						lv_my_jd.setVisibility(View.VISIBLE);
						notice.setVisibility(View.GONE);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

}
