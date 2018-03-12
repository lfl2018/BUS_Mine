package com.mogu.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.Url;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;
import com.tencent.mm.sdk.platformtools.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class JuDianDetailActivity extends Activity {
	private ImageView ivPic;
	private TextView tv_lv;
	private TextView tv_dis;
	private TextView update;
	private String LV;
	private String wifibh;
	private int i;
	private UpDateHandler upDateHandler;
	private TextView tvName;
	private String judianName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_judian_detail);
		TitleUtils.setTitle(JuDianDetailActivity.this, R.id.topbar, "据点详情");
		upDateHandler = new UpDateHandler();
		LV = getIntent().getStringExtra("LV");
		wifibh = getIntent().getStringExtra("wifibh");
		judianName = getIntent().getStringExtra(Constant.JUDIAN_NAME);
		
		tvName = (TextView) findViewById(R.id.tv_name);
		ivPic = (ImageView) findViewById(R.id.img_jd);
		tv_lv = (TextView) findViewById(R.id.tv_lv);
		tv_dis = (TextView) findViewById(R.id.tv_lv_dis);
		update = (TextView) findViewById(R.id.tv_update);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(JuDianDetailActivity.this).setMessage("确定升级？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						upDatejd();
					}
				}).setNegativeButton("取消", null).show();
			}

		});
		doSomething();
	}

	/**
	 * 据点升级
	 */
	private void upDatejd() {
		SessionRequestParams params = new SessionRequestParams(
				MrLiUrl.URL_JuDianUpDate);
		params.addBodyParameter("wifibh", wifibh);
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
				
			}

			@Override
			public void success(String result) throws JSONException {
				JSONObject json = new JSONObject(result);
				if (json.getString("code").equals("1")) {
					i++;
					Message message=new Message(); 
					message.what = i;
					upDateHandler.sendMessage(message);
					ToastShow.shortShowToast("升级成功");
				}else {
					ToastShow.shortShowToast(json.getString("msg"));
				}
			}
		});
	}

	/**
	 * 据点描述
	 */
	private void doSomething() {
		
		tvName.setText(judianName);
		
		if ("001".equals(LV)) {
			i = 1;
			ivPic.setImageResource(R.drawable.icon_my_level_first);
			tv_lv.setText("当前等级为 ：1");
			tv_dis.setText("最多可获取50点积分\t升级需要25积分");
		} else if ("002".equals(LV)) {
			i = 2;
			ivPic.setImageResource(R.drawable.icon_my_level_second);
			tv_lv.setText("当前等级为 ：2");
			tv_dis.setText("最多可获取150点积分\t升级需要50积分");
		} else if ("003".equals(LV)) {
			i = 3;
			update.setVisibility(View.GONE);
			ivPic.setImageResource(R.drawable.icon_my_level_third);
			tv_lv.setText("当前等级为 ：3");
			tv_dis.setText("已升至最高级\t最多可获取500点积分");
		}
	}

	class UpDateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				ivPic.setImageResource(R.drawable.icon_my_level_second);
				tv_lv.setText("当前等级为 ：2");
				tv_dis.setText("最多可获取150点积分\t升级需要50积分");
				break;
			case 3:
				update.setVisibility(View.GONE);
				ivPic.setImageResource(R.drawable.icon_my_level_third);
				tv_lv.setText("当前等级为 ：3");
				tv_dis.setText("已升至最高级\t最多可获取500点积分");
				break;

			default:
				break;
			}
		}
	}
}
