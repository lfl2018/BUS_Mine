package com.mogu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.R;
import com.mogu.adapter.OtherJudianAdapter;
import com.mogu.app.Url;
import com.mogu.entity.mywifilist.MyWifiList;
import com.mogu.entity.mywifilist.WifiList2;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.SessionRequestParams;

public class OtherJudianFragment extends Fragment{
	private View Mlayout;
	private OtherJudianAdapter adapter;
	private ListView lv_my_jd;
	private ArrayList<WifiList2>list = new ArrayList<WifiList2>();
	private TextView notice;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Mlayout == null) {
			Mlayout = inflater.inflate(R.layout.fragment_other_judian, null);
			initViews(Mlayout);
		}
		return Mlayout;
	}
	private void initViews(View view) {
		// TODO Auto-generated method stub
		notice = (TextView) view.findViewById(R.id.tv_notice);
		lv_my_jd = (ListView) view.findViewById(R.id.lv_other_jd);
		adapter = new OtherJudianAdapter(getActivity(),list);
		lv_my_jd.setAdapter(adapter);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getdata();
	}
	private void getdata() {
		SessionRequestParams params = new SessionRequestParams(Url.MyShareWifi_URL);
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
					List<WifiList2>mlist = new ArrayList<WifiList2>();
					mlist = json.getWifiList2();
					list.clear();
					list.addAll(mlist);
					if (list.size()==0) {
						lv_my_jd.setVisibility(View.GONE);
						notice.setVisibility(View.VISIBLE);
					}else {
						lv_my_jd.setVisibility(View.VISIBLE);
						notice.setVisibility(View.GONE);
					}
					adapter.notifyDataSetChanged();
					
				}else {
					Log.e("a", json.getMsg());
				}
			}
		});
	}
	
	
}
