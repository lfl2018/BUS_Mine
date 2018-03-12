package com.mogu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import com.mogu.adapter.MissionAdapter;
import com.mogu.app.Url;
import com.mogu.entity.mission.Mission;
import com.mogu.entity.mission.MrrwList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
/**
 * 任务中心
 * @author Administrator
 *
 */
public class MissionActivity extends Activity{
	
	private ListView lv_mission;
	private MissionAdapter adapter;
	//数据源
	private ArrayList<MrrwList>list = new ArrayList<MrrwList>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission);
		TitleUtils.setTitle(this, R.id.topbar, "任务中心");
		initViews();
		getData();
	}


	private void initViews() {
		lv_mission = (ListView) findViewById(R.id.lv_mission);
		adapter = new MissionAdapter(MissionActivity.this, list);
		lv_mission.setAdapter(adapter);
	}
	
	private void getData() {
		SessionRequestParams params = new SessionRequestParams(Url.Mission_List);
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
				Log.e("妈妈", result);
				Mission json = GsonUtils.parseJSON(result, Mission.class);
				if (json.getCode().equals("1")) {
					List<MrrwList>mlist = new ArrayList<MrrwList>();
					mlist =  json.getMrrwList();
					list.addAll(mlist);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	
	
}
