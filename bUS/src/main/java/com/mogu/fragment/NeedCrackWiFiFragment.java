package com.mogu.fragment;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mogu.activity.R;
import com.mogu.activity.ShareConectwifiActivity;
import com.mogu.adapter.NeedCrackWifiListAdapter;
import com.mogu.app.Constant;
import com.mogu.utils.ToastShow;
import com.mogu.utils.WifiUtils;
import com.mogu.view.WifiPswDialog;
import com.mogu.view.WifiPswDialog.OnCustomDialogListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NeedCrackWiFiFragment extends Fragment {
	private View mLayout;
	private ListView needCrackWifiListView;
	private NeedCrackWifiListAdapter adapter;
	private String wifiPassword = null;
	private WifiUtils localWifiUtils;
	private String wifiItemSSID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_need_crack_wifi,
					container, false);
			localWifiUtils = new WifiUtils(getActivity());
			localWifiUtils.getConfiguration();
			initView(mLayout);
		}
		return mLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// localWifiUtils=((IndexFragment) getParentFragment()).localWifiUtils;

	}

	private void initView(View view) {

		needCrackWifiListView = (ListView) view.findViewById(R.id.lv_wf);

		adapter = new NeedCrackWifiListAdapter(getActivity(),
				((IndexFragment) getParentFragment()).needCrackWifiList);
		needCrackWifiListView.setAdapter(adapter);
		needCrackWifiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int wifiState = localWifiUtils.WifiCheckState();
				if (wifiState == WifiManager.WIFI_STATE_DISABLED
						|| wifiState == WifiManager.WIFI_STATE_DISABLING) {
					ToastShow.longShowToast("请打开WiFi！");
					return;
				}
				
				// linkWiFi(view, position);
				if (WifiUtils.getEncryptWay(adapter.getItem(position)) == 0) {
					
					int netId = localWifiUtils.
							CreateWifiInfo2(adapter.getItem(position),"");
					if (netId != -1) {
						if (localWifiUtils.ConnectWifi(netId)) {
							((IndexFragment) getParentFragment())
							.showLinkWiFiDialog();
//							Toast.makeText(getActivity(), "连接成功！",
//									Toast.LENGTH_SHORT).show();
//							((IndexFragment) getParentFragment())
//									.updateCurrentWiFiInfo();
						} else {
							Toast.makeText(getActivity(), "连接失败！",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), "网络连接错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Intent intent = new Intent(getActivity(),
							ShareConectwifiActivity.class);
					intent.putExtra(Constant.CLICK_WIFI_INFO,
							adapter.getItem(position));
					startActivity(intent);
				}
				

			}
		});
	}

	private void linkWiFi(View view, int position) {

		Log.i("ListOnItemClickListener", "start");
		// View selectedItem = view;
		// selectedItem.setBackgroundResource(R.color.gray);// 点击的Item项背景设置
		String wifiItem = adapter.getItem(position).SSID;// 获得选中的设备
		String[] ItemValue = wifiItem.split("--");
		wifiItemSSID = ItemValue[0];
		Log.i("ListOnItemClickListener", wifiItemSSID);

		int wifiItemId = localWifiUtils.IsConfiguration(
				((IndexFragment) getParentFragment()).wifiResultList, "\""
						+ wifiItemSSID + "\"");
		Log.i("ListOnItemClickListener", String.valueOf(wifiItemId));
		if (wifiItemId != -1) {
			// if (localWifiUtils.ConnectWifi(wifiItemId)) {// 连接指定WIFI
			// // selectedItem.setBackgroundResource(R.color.blue);
			// }
			if (localWifiUtils.ConnectWifi(wifiItemId)) {
				Toast.makeText(getActivity(), "连接成功！", Toast.LENGTH_SHORT)
						.show();
				((IndexFragment) getParentFragment()).updateCurrentWiFiInfo();
			} else {
				Toast.makeText(getActivity(), "连接失败！", Toast.LENGTH_SHORT)
						.show();
			}
		} else {// 没有配置好信息，配置
			WifiPswDialog pswDialog = new WifiPswDialog(getActivity(),
					new OnCustomDialogListener() {
						@Override
						public void back(String str) {
							wifiPassword = str;
							if (wifiPassword != null) {
								int netId = localWifiUtils
										.AddWifiConfig(
												((IndexFragment) getParentFragment()).wifiResultList,
												wifiItemSSID, wifiPassword);
								Log.i("WifiPswDialog", String.valueOf(netId));
								if (netId != -1) {
									localWifiUtils.getConfiguration();// 添加了配置信息，要重新得到配置信息
									if (localWifiUtils.ConnectWifi(netId)) {
										Toast.makeText(getActivity(), "连接成功！",
												Toast.LENGTH_SHORT).show();
										((IndexFragment) getParentFragment())
												.updateCurrentWiFiInfo();
									} else {
										Toast.makeText(getActivity(), "连接失败！",
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(getActivity(), "网络连接错误",
											Toast.LENGTH_SHORT).show();
									// selectedItem
									// .setBackgroundResource(R.color.burlywood);
								}
							} else {
								// selectedItem
								// .setBackgroundResource(R.color.burlywood);
							}
						}
					});
			pswDialog.show();
		}

	}

	public void refresh() {
		adapter.notifyDataSetChanged();
	}

}
