package com.mogu.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.mogu.app.Url;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.WifiUtils;
import com.mogu.utils.WifiUtils.WifiCipherType;

public class ShareWifiActivity extends Activity implements OnClickListener,
		AMapLocationListener {
	private IntentFilter filter;
	private EditText name;
	private EditText pass;
	private TextView ssd;
	private Button btn_share;
	private WifiUtils wifiUtils;
	private String bssid;
	private String Strssid;
	private Double lat;
	private Double lgt;
	private WifiCipherType security;
	private NetworkConnectChangedReceiver receiver;
	private boolean shared = false;
	private ProgressDialog dialog;
	private MyHandler mhandler;
	private TextView tv_cuowu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share);
		intiLoca();
		name = (EditText) findViewById(R.id.edt_name);
		pass = (EditText) findViewById(R.id.edt_pass);
		ssd = (TextView) findViewById(R.id.tv_ssd);
		tv_cuowu = (TextView) findViewById(R.id.tv_cuowu);
		wifiUtils = new WifiUtils(ShareWifiActivity.this);
		wifiUtils.getConnectedInfo();
		ssd.setText(wifiUtils.getConnectedSSID());
		bssid = wifiUtils.getConnectedBSSID();
		Log.e("mama", bssid);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(this);
		Strssid = wifiUtils.getConnectedSSID();
		mhandler = new MyHandler();
	}

	private void registBC() {
		if (filter != null) {
			return;
		}
		filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkConnectChangedReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:

			if (lat != null) {

				createDialog();

				checkWifi();
				// ShraeWifi();
				registBC();
			}

			break;

		default:
			break;
		}
	}

	private void createDialog() {
		dialog = new ProgressDialog(ShareWifiActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("连接中...");
		dialog.show();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);// 让他显示10秒后，取消ProgressDialog

				} catch (InterruptedException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mhandler.sendEmptyMessage(1);
			}
		});
		t.start();

	}

	private void checkWifi() {
		String Strpass = pass.getText().toString();
		String myssid = Strssid.replaceAll("\"", "");
		if (security == null) {
			security = wifiUtils.getSecurity(myssid);
		}
		int networkId = wifiUtils.shareWifiInfo(myssid, Strpass, security);

		if (wifiUtils.ConnectWifi(networkId)) {
		} else {
		}
	}

	private void shareWifi() {
		shared = true;
		String Strname = name.getText().toString();
		String Strpass = pass.getText().toString();
		SessionRequestParams params = new SessionRequestParams(
				Url.Share_wifi_URL);
		String myssid = Strssid.replaceAll("\"", "");
		params.addBodyParameter("ssid00", myssid);
		params.addBodyParameter("mac000", bssid);
		params.addBodyParameter("passwd", Strpass);
		params.addBodyParameter("jingdu", lgt + "");
		params.addBodyParameter("weidu0", lat + "");
		params.addBodyParameter("fenxmc", Strname);

		x.http().post(params, new HttpCallBack() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				arg0.printStackTrace();
				Toast.makeText(x.app(), "分享失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFinished() {
				shared = false;

			}

			@Override
			public void success(String arg0) {
				try {
					JSONObject json = new JSONObject(arg0);
					if ("1".equals(json.get("code"))) {
						dialog.dismiss();
						tv_cuowu.setVisibility(View.GONE);
						Toast.makeText(ShareWifiActivity.this, "分享成功",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						dialog.dismiss();
						tv_cuowu.setVisibility(View.GONE);
						Toast.makeText(ShareWifiActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public AMapLocationClientOption mLocationOption = null;
	public AMapLocationClient mlocationClient = null;

	private void intiLoca() {
		mlocationClient = new AMapLocationClient(ShareWifiActivity.this);
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位监听
		mlocationClient.setLocationListener(this);
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		// 设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		// 启动定位
		mlocationClient.startLocation();
	}

	// @Override
	// public void onLocationChanged(AMapLocation amapLocation) {
	// if (amapLocation != null) {
	// if (amapLocation.getErrorCode() == 0) {
	// //定位成功回调信息，设置相关消息
	// amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
	// amapLocation.getLatitude();//获取纬度
	// amapLocation.getLongitude();//获取经度
	// amapLocation.getAccuracy();//获取精度信息
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Date date = new Date(amapLocation.getTime());
	// df.format(date);//定位时间
	// } else {
	// //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
	// Log.e("AmapError","location Error, ErrCode:"
	// + amapLocation.getErrorCode() + ", errInfo:"
	// + amapLocation.getErrorInfo());
	// }
	// }
	// }

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				// 定位成功回调信息，设置相关消息
				// amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
				// amapLocation.getLatitude();// 获取纬度
				// amapLocation.getLongitude();// 获取经度
				// amapLocation.getAccuracy();// 获取精度信息
				lat = amapLocation.getLatitude();
				lgt = amapLocation.getLongitude();

				// Log.e("爸爸", amapLocation.getAddress()+lat+"  "+lgt);
			} else {
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				// Log.e("AmapError",
				// "location Error, ErrCode:"
				// + amapLocation.getErrorCode() + ", errInfo:"
				// + amapLocation.getErrorInfo());
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	public class NetworkConnectChangedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (WifiManager.WIFI_STATE_CHANGED_ACTION
					.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
				int wifiState = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE, 0);
				Log.e("H3c", "wifiState" + wifiState);
				switch (wifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
					Toast.makeText(ShareWifiActivity.this, "请打开wifi",
							Toast.LENGTH_SHORT).show();
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					break;
				//
				}
			}
			// 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
			// 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {
				Parcelable parcelableExtra = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (null != parcelableExtra) {
					NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
					State state = networkInfo.getState();
					boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
					Log.e("H3c", "isConnected" + isConnected);
					if (isConnected) {
						Log.e("爸爸", "爸爸成功");
						wifiUtils.getConnectedInfo();
						String mymac = wifiUtils.getConnectedBSSID();
						if (bssid.equals(mymac)) {
							if (shared) {
								return;
							}
							shareWifi();
							Log.e("爸爸", "爸爸大成功");
						}
						// Toast.makeText(ShareWifiActivity.this, "已连接",
						// Toast.LENGTH_SHORT).show();
					} else {
						Log.e("爸爸", "连接失败买了个表");
						// Toast.makeText(ShareWifiActivity.this, "连接失败买了个表",
						// Toast.LENGTH_SHORT).show();
					}
				}
			}
			// 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
			// 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
			// 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {
				ConnectivityManager manager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo gprs = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				Log.e("baba",
						"网络状态改变:" + wifi.isConnected() + " 3g:"
								+ gprs.isConnected());
				NetworkInfo info = intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				if (info != null) {
					Log.e("H3c", "info.getTypeName()" + info.getTypeName());
					Log.e("H3c", "getSubtypeName()" + info.getSubtypeName());
					Log.e("H3c", "getState()" + info.getState());
					Log.e("H3c", "getDetailedState()"
							+ info.getDetailedState().name());
					Log.e("H3c", "getDetailedState()" + info.getExtraInfo());
					Log.e("H3c", "getType()" + info.getType());

					if (NetworkInfo.State.CONNECTED == info.getState()) {
					} else if (info.getType() == 1) {
						if (NetworkInfo.State.DISCONNECTING == info.getState()) {

						}
					}
				}
			}
		}
	}

	class MyHandler extends Handler {
		public MyHandler() {
			super();
		}

		public MyHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				tv_cuowu.setVisibility(View.VISIBLE);
				dialog.dismiss();
				break;

			default:
				break;
			}
		}
	}

}
