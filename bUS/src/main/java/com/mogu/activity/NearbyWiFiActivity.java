package com.mogu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mogu.adapter.NearbyWifiListAdapter;
import com.mogu.app.Url;
import com.mogu.entity.nearwf.NearWF;
import com.mogu.entity.nearwf.WifiList1;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

public class NearbyWiFiActivity extends Activity {

	private TextView nearbyAllWiFiTextView;
	private ListView nearbyWiFiListView;
	private NearbyWifiListAdapter adapter;
	private ArrayList<WifiList1>list = new ArrayList<WifiList1>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_wifi);
		TitleUtils.setTitle(this, R.id.topbar, "周边WiFi");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		getMyWifiList();
		nearbyAllWiFiTextView = (TextView) findViewById(R.id.tv_nearby_all_wifi);
		nearbyAllWiFiTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();  
				  
				intent.setClass(NearbyWiFiActivity.this, BasicMapActivity.class);  
				intent.putExtra("NO", "2");
				intent.putExtra("list", (Serializable)list);
				
				startActivity(intent);  
				
//				startActivity(new Intent(NearbyWiFiActivity.this,
//						NearbyAllWiFiActivity.class));
			}
		});
		nearbyWiFiListView = (ListView) findViewById(R.id.lv_nearby_wifi);

		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		adapter = new NearbyWifiListAdapter(this,list);
		nearbyWiFiListView.setAdapter(adapter);
		nearbyWiFiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 路线规划
				Intent intent = new Intent();  
				  
				intent.setClass(NearbyWiFiActivity.this, BasicMapActivity.class);  
				intent.putExtra("NO", "1");
				intent.putExtra("la", list.get(position).getWeidu0());
				intent.putExtra("lg", list.get(position).getJingdu());
				intent.putExtra("name", list.get(position).getFenxmc());
				
				startActivity(intent);  

			}
		});

	}
	
	private String lgt = "118.115129";
	private String lat = "24.470718";
	private void getMyWifiList(){
		lgt = getIntent().getStringExtra("jingdu");
		lat = getIntent().getStringExtra("weidu0");
		if (TextUtils.isEmpty(lat)) {
			lat = "24.470718";
		}
		if (TextUtils.isEmpty(lgt)) {
			lgt = "118.115129";
		}
		SessionRequestParams params =new SessionRequestParams(Url.NearbyWFList_URL);
		params.addBodyParameter("jingdu", lgt);
		params.addBodyParameter("weidu0", lat);
		
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
				NearWF json = GsonUtils.parseJSON(arg0, NearWF.class);
				if (json.getCode().equals("1")) {
					List<WifiList1>mlist = new ArrayList<WifiList1>();
					mlist = json.getWifiList1();
					list.addAll(mlist);
					adapter.notifyDataSetChanged();
				}else {
					Toast.makeText(NearbyWiFiActivity.this, json.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
}
