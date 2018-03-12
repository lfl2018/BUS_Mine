package com.mogu.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.util.Log;

public class WifiUtils {

	/** 定义几种加密方式，一种是WEP，一种是WPA/WPA2，还有没有密码的情况 */
	public enum WifiCipherType {
		WIFI_CIPHER_WEP, WIFI_CIPHER_WPA_EAP, WIFI_CIPHER_WPA_PSK, WIFI_CIPHER_WPA2_PSK, WIFI_CIPHER_NOPASS
	}

	private WifiManager localWifiManager;// 提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
	// private List<ScanResult>
	// wifiScanList;//ScanResult用来描述已经检测出的接入点，包括接入的地址、名称、身份认证、频率、信号强度等
	private List<WifiConfiguration> wifiConfigList;// WIFIConfiguration描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
	private WifiInfo wifiConnectedInfo;// 已经建立好网络链接的信息
	private WifiLock wifiLock;// 手机锁屏后，阻止WIFI也进入睡眠状态及WIFI的关闭

	public WifiUtils(Context context) {
		localWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	// 检查WIFI状态
	public int WifiCheckState() {
		return localWifiManager.getWifiState();
	}

	// 开启WIFI
	public void WifiOpen() {
		if (!localWifiManager.isWifiEnabled()) {
			localWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭WIFI
	public void WifiClose() {
		if (!localWifiManager.isWifiEnabled()) {
			localWifiManager.setWifiEnabled(false);
		}
	}

	// 扫描wifi
	public void WifiStartScan() {
		localWifiManager.startScan();
	}

	// 得到Scan结果
	public List<ScanResult> getScanResults() {
		return localWifiManager.getScanResults();// 得到扫描结果
	}

	// Scan结果转为Sting
	public List<String> scanResultToString(List<ScanResult> list) {
		List<String> strReturnList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			ScanResult strScan = list.get(i);
			String str = strScan.toString();
			boolean bool = strReturnList.add(str);
			if (!bool) {
				Log.i("scanResultToSting", "Addfail");
			}
		}
		return strReturnList;
	}

	// 得到Wifi配置好的信息
	public void getConfiguration() {
		wifiConfigList = localWifiManager.getConfiguredNetworks();// 得到配置好的网络信息
		if (null == wifiConfigList) {
			wifiConfigList = new ArrayList<WifiConfiguration>();
		}
		for (int i = 0; i < wifiConfigList.size(); i++) {
			Log.i("getConfiguration", wifiConfigList.get(i).SSID);
			Log.i("getConfiguration",
					String.valueOf(wifiConfigList.get(i).networkId));
		}
	}

	// 判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
	public int IsConfiguration(List<ScanResult> wifiResultList, String SSID) {
		Log.i("IsConfiguration", String.valueOf(wifiConfigList.size()));
		for (int i = 0; i < wifiConfigList.size(); i++) {
			Log.i(wifiConfigList.get(i).SSID,
					String.valueOf(wifiConfigList.get(i).networkId));
			if (wifiConfigList.get(i).SSID.equals(SSID)) {// 地址相同
				return wifiConfigList.get(i).networkId;
			}
		}
		for (int i = 0; i < wifiResultList.size(); i++) {
			String mSSID = "\"" + wifiResultList.get(i).SSID + "\"";
			Log.i(mSSID, String.valueOf(wifiResultList.get(i).capabilities));
			if (mSSID.equals(SSID)) {// 地址相同
				return isNeedPwd(wifiResultList.get(i));
			}
		}
		return -1;
	}

	/**
	 * 是否需要密码，不需要密码返回networkId，需要密码返回-1
	 * 
	 * @param wifiinfo
	 * @return
	 */
	public int isNeedPwd(ScanResult wifiinfo) {
		String capabilities = "";

		if (wifiinfo.capabilities.contains("WPA2-PSK")) {
			// WPA-PSK加密
			capabilities = "psk2";
		} else if (wifiinfo.capabilities.contains("WPA-PSK")) {
			// WPA-PSK加密
			capabilities = "psk";
		} else if (wifiinfo.capabilities.contains("WPA-EAP")) {
			// WPA-EAP加密
			capabilities = "eap";
		} else if (wifiinfo.capabilities.contains("WEP")) {
			// WEP加密
			capabilities = "wep";
		} else {
			// 无密码
			capabilities = "";
		}

		if (!capabilities.equals("")) {
			return -1;
		} else {
			WifiConfiguration config = CreateWifiInfo(wifiinfo.SSID,
					wifiinfo.BSSID, "", WifiCipherType.WIFI_CIPHER_NOPASS);
			if (config != null) {
				return localWifiManager.addNetwork(config);
			} else {
				return -1;
			}
		}
	}

	// 添加指定WIFI的配置信息,原列表不存在此SSID
	public int AddWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
		int wifiId = -1;
		for (int i = 0; i < wifiList.size(); i++) {
			ScanResult wifi = wifiList.get(i);
			if (wifi.SSID.equals(ssid)) {
				Log.i("AddWifiConfig", "equals");
				WifiConfiguration wifiCong = new WifiConfiguration();
				wifiCong.SSID = "\"" + wifi.SSID + "\"";// \"转义字符，代表"
				wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK密码
				wifiCong.hiddenSSID = false;
				wifiCong.status = WifiConfiguration.Status.ENABLED;
				wifiId = localWifiManager.addNetwork(wifiCong);// 将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
				if (wifiId != -1) {
					return wifiId;
				}
			}
		}
		return wifiId;
	}

	public int CreateWifiInfo2(ScanResult wifiinfo, String pwd) {
		WifiCipherType type;

		if (wifiinfo.capabilities.contains("WPA2-PSK")) {
			// WPA-PSK加密
			type = WifiCipherType.WIFI_CIPHER_WPA2_PSK;
		} else if (wifiinfo.capabilities.contains("WPA-PSK")) {
			// WPA-PSK加密
			type = WifiCipherType.WIFI_CIPHER_WPA_PSK;
		} else if (wifiinfo.capabilities.contains("WPA2-EAP")) {
			// WPA-EAP加密
			type = WifiCipherType.WIFI_CIPHER_WPA_EAP;
		} else if (wifiinfo.capabilities.contains("WPA-EAP")) {
			// WPA-EAP加密
			type = WifiCipherType.WIFI_CIPHER_WPA_EAP;
		} else if (wifiinfo.capabilities.contains("WEP")) {
			// WEP加密
			type = WifiCipherType.WIFI_CIPHER_WEP;
		} else {
			// 无密码
			type = WifiCipherType.WIFI_CIPHER_NOPASS;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			WifiConfiguration config = isExsits(wifiinfo.SSID);
			if (config != null) {
				getConnectedInfo();
				int connectedID = getConnectedID();
				localWifiManager.disableNetwork(connectedID);
				localWifiManager.disconnect();
				return config.networkId;
			}
		}

		WifiConfiguration config = CreateWifiInfo(wifiinfo.SSID,
				wifiinfo.BSSID, pwd, type);
		if (config != null) {
			getConnectedInfo();
			int connectedID = getConnectedID();
			localWifiManager.disableNetwork(connectedID);
			localWifiManager.disconnect();

			return localWifiManager.addNetwork(config);
		} else {
			return -1;
		}
	}

	/**
	 * 得到当前密码类型
	 * 
	 * @param SSID
	 * @return
	 */
	public WifiCipherType getSecurity(String SSID) {
		WifiConfiguration config = isExsits(SSID);
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return WifiCipherType.WIFI_CIPHER_WPA_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return WifiCipherType.WIFI_CIPHER_WPA_EAP;
		}
		return (config.wepKeys[0] != null) ? WifiCipherType.WIFI_CIPHER_WEP
				: WifiCipherType.WIFI_CIPHER_NOPASS;
	}

	public void clearcofig(String SSID) {
		WifiConfiguration config1 = this.isExsits(SSID);
		if (config1 != null) {
			//
			localWifiManager.removeNetwork(config1.networkId);
			localWifiManager.saveConfiguration();
		}
	}

	/** 分享 */
	public int shareWifiInfo(String SSID, String password, WifiCipherType type) {
		// WifiCipherType type=getSecurity(SSID);

		// String BSSID=wifiinfo.BSSID;

		int priority;

		WifiConfiguration config1 = this.isExsits(SSID);
		if (config1 != null) {
			//
			localWifiManager.removeNetwork(config1.networkId);
			localWifiManager.saveConfiguration();
		}

		WifiConfiguration config = new WifiConfiguration();
		/* 清除之前的连接信息 */
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		config.status = WifiConfiguration.Status.ENABLED;

		priority = getMaxPriority() + 1;
		if (priority > 99999) {
			priority = shiftPriorityAndSave();
		}

		config.priority = priority; // 2147483647;
		/* 各种加密方式判断 */
		if (type == WifiCipherType.WIFI_CIPHER_NOPASS) {
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (type == WifiCipherType.WIFI_CIPHER_WEP) {
			// config.preSharedKey = "\"" + password + "\"";
			//
			// config.allowedAuthAlgorithms
			// .set(WifiConfiguration.AuthAlgorithm.SHARED);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			// config.allowedGroupCiphers
			// .set(WifiConfiguration.GroupCipher.WEP104);
			// config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;

			// 正确的写法
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			int length = password.length();
			// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
			if ((length == 10 || length == 26 || length == 58)
					&& password.matches("[0-9A-Fa-f]*")) {
				config.wepKeys[0] = password;
			} else {
				config.wepKeys[0] = '"' + password + '"';
			}

		} else if (type == WifiCipherType.WIFI_CIPHER_WPA_EAP) {

			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN
					| WifiConfiguration.Protocol.WPA);

		} else if (type == WifiCipherType.WIFI_CIPHER_WPA_PSK) {

			config.preSharedKey = "\"" + password + "\"";
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN
					| WifiConfiguration.Protocol.WPA);

		} else if (type == WifiCipherType.WIFI_CIPHER_WPA2_PSK) {

			config.preSharedKey = "\"" + password + "\"";
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		} else {
			return -1;
		}
		int networkId = localWifiManager.addNetwork(config);
		return networkId;
	}

	/** 配置一个连接 */
	public WifiConfiguration CreateWifiInfo(String SSID, String BSSID,
			String password, WifiCipherType type) {

		int priority;

		WifiConfiguration config = this.isExsits(SSID);
		if (config != null) {
			// 本机之前配置过此wifi热点，调整优先级后，直接返回
			return setMaxPriority(config);
		}

		config = new WifiConfiguration();
		/* 清除之前的连接信息 */
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		config.status = WifiConfiguration.Status.ENABLED;

		priority = getMaxPriority() + 1;
		if (priority > 99999) {
			priority = shiftPriorityAndSave();
		}

		config.priority = priority; // 2147483647;
		/* 各种加密方式判断 */
		if (type == WifiCipherType.WIFI_CIPHER_NOPASS) {
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (type == WifiCipherType.WIFI_CIPHER_WEP) {
			// config.preSharedKey = "\"" + password + "\"";
			//
			// config.allowedAuthAlgorithms
			// .set(WifiConfiguration.AuthAlgorithm.SHARED);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			// config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			// config.allowedGroupCiphers
			// .set(WifiConfiguration.GroupCipher.WEP104);
			// config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;

			// 正确的写法
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			int length = password.length();
			// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
			if ((length == 10 || length == 26 || length == 58)
					&& password.matches("[0-9A-Fa-f]*")) {
				config.wepKeys[0] = password;
			} else {
				config.wepKeys[0] = '"' + password + '"';
			}
		} else if (type == WifiCipherType.WIFI_CIPHER_WPA_EAP) {

			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN
					| WifiConfiguration.Protocol.WPA);

		} else if (type == WifiCipherType.WIFI_CIPHER_WPA_PSK) {

			config.preSharedKey = "\"" + password + "\"";
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN
					| WifiConfiguration.Protocol.WPA);

		} else if (type == WifiCipherType.WIFI_CIPHER_WPA2_PSK) {

			config.preSharedKey = "\"" + password + "\"";
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		} else {
			return null;
		}

		return config;
	}

	public WifiConfiguration setMaxPriority(WifiConfiguration config) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return config;
		}

		int priority = getMaxPriority() + 1;
		if (priority > 99999) {
			priority = shiftPriorityAndSave();
		}

		config.priority = priority;
		localWifiManager.updateNetwork(config);

		// 本机之前配置过此wifi热点，直接返回
		return config;
	}

	private int shiftPriorityAndSave() {
		List<WifiConfiguration> localList = this.localWifiManager
				.getConfiguredNetworks();
		sortByPriority(localList);
		int i = localList.size();
		for (int j = 0;; ++j) {
			if (j >= i) {
				this.localWifiManager.saveConfiguration();
				return i;
			}
			WifiConfiguration localWifiConfiguration = (WifiConfiguration) localList
					.get(j);
			localWifiConfiguration.priority = j;
			this.localWifiManager.updateNetwork(localWifiConfiguration);
		}
	}

	private int getMaxPriority() {
		List<WifiConfiguration> localList = this.localWifiManager
				.getConfiguredNetworks();
		int i = 0;
		Iterator<WifiConfiguration> localIterator = localList.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return i;
			WifiConfiguration localWifiConfiguration = (WifiConfiguration) localIterator
					.next();
			if (localWifiConfiguration.priority <= i)
				continue;
			i = localWifiConfiguration.priority;
		}
	}

	/** 查看以前是否也配置过这个网络 */
	public WifiConfiguration isExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = localWifiManager
				.getConfiguredNetworks();

		for (WifiConfiguration existingConfig : existingConfigs) {

			if (existingConfig.SSID.toString().equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	// 连接指定Id的WIFI
	public boolean ConnectWifi(int wifiId) {
		getConfiguration();
		for (int i = 0; i < wifiConfigList.size(); i++) {
			WifiConfiguration wifi = wifiConfigList.get(i);
			if (wifi.networkId == wifiId) {
				while (!(localWifiManager.enableNetwork(wifiId, true))) {// 激活该Id，建立连接
					Log.i("ConnectWifi",
							String.valueOf(wifiConfigList.get(wifiId).status));// status:0--已经连接，1--不可连接，2--可以连接
				}
				localWifiManager.saveConfiguration();
				localWifiManager.reconnect();
				return true;
			}
		}
		return false;
	}

	// 连接指定BSSID的WIFI
	public boolean ConnectWifi(String SSID) {
		getConfiguration();
		for (int i = 0; i < wifiConfigList.size(); i++) {
			WifiConfiguration wifi = wifiConfigList.get(i);
			if (SSID.equals(wifi.SSID)) {
				while (!(localWifiManager.enableNetwork(wifi.networkId, true))) {// 激活该Id，建立连接
					Log.i("ConnectWifi", String.valueOf(wifi.status));// status:0--已经连接，1--不可连接，2--可以连接
				}
				return true;
			}
		}
		return false;
	}

	// 创建一个WIFILock
	public void createWifiLock(String lockName) {
		wifiLock = localWifiManager.createWifiLock(lockName);
	}

	// 锁定wifilock
	public void acquireWifiLock() {
		wifiLock.acquire();
	}

	// 解锁WIFI
	public void releaseWifiLock() {
		if (wifiLock.isHeld()) {// 判定是否锁定
			wifiLock.release();
		}
	}

	// 得到建立连接的信息
	public void getConnectedInfo() {
		wifiConnectedInfo = localWifiManager.getConnectionInfo();
	}

	/*
	 * 得到连接的MAC地址
	 */
	public String getConnectedBSSID() {
		return (wifiConnectedInfo == null) ? "NULL" : wifiConnectedInfo
				.getBSSID();
	}

	/*
	 * 得到本机的MAC地址
	 */
	public String getConnectedMacAddr() {
		return (wifiConnectedInfo == null) ? "NULL" : wifiConnectedInfo
				.getMacAddress();
	}

	// 得到连接的名称SSID
	public String getConnectedSSID() {
		return (wifiConnectedInfo == null) ? "NULL" : wifiConnectedInfo
				.getSSID();
	}

	// 得到连接的IP地址
	public int getConnectedIPAddr() {
		return (wifiConnectedInfo == null) ? 0 : wifiConnectedInfo
				.getIpAddress();
	}

	// 得到连接的ID
	public int getConnectedID() {
		return (wifiConnectedInfo == null) ? 0 : wifiConnectedInfo
				.getNetworkId();
	}

	private void sortByPriority(List<WifiConfiguration> paramList) {
		Collections.sort(paramList, new SjrsWifiManagerCompare());
	}

	class SjrsWifiManagerCompare implements Comparator<WifiConfiguration> {
		public int compare(WifiConfiguration paramWifiConfiguration1,
				WifiConfiguration paramWifiConfiguration2) {
			return paramWifiConfiguration1.priority
					- paramWifiConfiguration2.priority;
		}
	}

	// 得到连接的ID
	public int getConnectedIsNeedPwd() {
		if (WifiCheckState() == WifiManager.WIFI_STATE_DISABLED
				|| WifiCheckState() == WifiManager.WIFI_STATE_DISABLING) {
			return -1;
		}
		getConnectedInfo();
		// 当前连接SSID
		String currentSSid = wifiConnectedInfo.getSSID();
		currentSSid = currentSSid.replace("\"", "");
		getConfiguration();
		for (WifiConfiguration wifiConfiguration : wifiConfigList) {
			// 配置过的SSID
			String configSSid = wifiConfiguration.SSID;
			configSSid = configSSid.replace("\"", "");

			// 比较networkId，防止配置网络保存相同的SSID
			if (currentSSid.equals(configSSid)
					&& wifiConnectedInfo.getNetworkId() == wifiConfiguration.networkId) {
				Log.e("hefeng", "当前网络安全性：" + getSecurity(wifiConfiguration));
				return getSecurity(wifiConfiguration);
			}
		}
		return 0;
	}

	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	static int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return SECURITY_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return SECURITY_EAP;
		}
		return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
	}

	public static int getEncryptWay(ScanResult wifiinfo) {
		String capabilities = "";

		if (wifiinfo.capabilities.contains("WPA2-PSK")) {
			// WPA-PSK加密
			capabilities = "psk2";
		} else if (wifiinfo.capabilities.contains("WPA-PSK")) {
			// WPA-PSK加密
			capabilities = "psk";
		} else if (wifiinfo.capabilities.contains("WPA2-EAP")) {
			// WPA-EAP加密
			capabilities = "eap2";
		} else if (wifiinfo.capabilities.contains("WPA-EAP")) {
			// WPA-EAP加密
			capabilities = "eap";
		} else if (wifiinfo.capabilities.contains("WEP")) {
			// WEP加密
			capabilities = "wep";
		} else {
			// 无密码
			capabilities = "";
		}

		if (!capabilities.equals("")) {
			return -1;
		} else {
			return 0;
		}
	}

}
