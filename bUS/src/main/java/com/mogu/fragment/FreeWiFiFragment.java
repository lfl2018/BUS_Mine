package com.mogu.fragment;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore2d.p;
import com.mogu.activity.NearbyWiFiActivity;
import com.mogu.activity.R;
import com.mogu.adapter.FreeWifiListAdapter;
import com.mogu.app.Url;
import com.mogu.entity.wifi.FreeWifiList;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;
import com.mogu.utils.WifiUtils;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FreeWiFiFragment extends Fragment {
	private View mLayout;
	private ListView freeWifiListView;
	private FreeWifiListAdapter adapter;
	private WifiUtils localWifiUtils;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_free_wifi, container,
					false);
			localWifiUtils = new WifiUtils(getActivity());

			initView(mLayout);

		}
		return mLayout;
	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState) {
	// super.onActivityCreated(savedInstanceState);
	// localWifiUtils=((IndexFragment) getParentFragment()).localWifiUtils;
	//
	// }

	private void initView(View view) {

		freeWifiListView = (ListView) view.findViewById(R.id.lv_wf);

		adapter = new FreeWifiListAdapter(getActivity(),
				((IndexFragment) getParentFragment()).freeWifiList);
		freeWifiListView.setAdapter(adapter);
		freeWifiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// localWifiUtils.WifiOpen();
				int wifiState = localWifiUtils.WifiCheckState();
				if (wifiState == WifiManager.WIFI_STATE_DISABLED
						|| wifiState == WifiManager.WIFI_STATE_DISABLING) {
					ToastShow.longShowToast("请打开WiFi！");
					return;
				}
				Builder builder = new Builder(getActivity())
						.setMessage("连接到此网络？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										// 此处加入连接wifi代码
										int netID = localWifiUtils.CreateWifiInfo2(
												adapter.getItem(position).scanResult,
												adapter.getItem(position)
														.getPasswd());
										if (netID != -1) {
											if (localWifiUtils
													.ConnectWifi(netID)) {
												// Toast.makeText(getActivity(),
												// "连接成功！", Toast.LENGTH_SHORT)
												// .show();
												// ((IndexFragment)
												// getParentFragment())
												// .updateCurrentWiFiInfo();

												((IndexFragment) getParentFragment())
														.showLinkWiFiDialog();
												updateLinkWiFiSuccess(adapter
														.getItem(position));

											} else {
												Toast.makeText(getActivity(),
														"连接失败！",
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											Toast.makeText(getActivity(),
													"网络连接错误",
													Toast.LENGTH_SHORT).show();
										}
										// ssd.setText("已成功连接到QCWL");
										// tv1.setText("Internet访问");

									}

								}).setNegativeButton("取消", null);
				builder.show();
			}
		});
	}

	private void updateLinkWiFiSuccess(FreeWifiList freeWifiList) {
		final SessionRequestParams params = new SessionRequestParams(Url.LINK_WIFI);
		params.addBodyParameter("fxhybh", freeWifiList.getHybh00());
		params.addBodyParameter("wifibh", freeWifiList.getWifibh());
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				x.http().post(params, new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {

					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
//						ToastShow.shortShowToast("success");
					}
				});
			}
		}, 1500);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	public void refresh() {
		adapter.notifyDataSetChanged();
	}
}
