package com.mogu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络类型
 * 
 */
public class NetworkUtil {

	/** WIFI */
	public static final int NETTYPE_WIFI = 1;
	/** 2G */
	public static final int NETTYPE_2G = 2;
	/** 3G */
	public static final int NETTYPE_3G = 3;
	/** 4G */
	public static final int NETTYPE_4G = 4;
	/** 不知道网络类型 */
	public static final int NETTYPE_UNKOWN = -1;
	/** 当前网络状态，默认-1 */
	public static int CURRENT_NETTYPE = NETTYPE_UNKOWN;

	/**
	 * 返回网络状态
	 * 
	 * @return 1为成功WiFi已连接，2为2G，3为3G，4为4G， -1为网络未连接
	 */
	/**
	 * @param context
	 * @return
	 */
	public static int checkNetworkStatus(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
				return (CURRENT_NETTYPE = NETTYPE_WIFI);
			} else {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				switch (telephonyManager.getNetworkType()) {
				/** (2.5G)移动和联通,sdk1.0 */
				case TelephonyManager.NETWORK_TYPE_GPRS:
					/** (2.75G)2.5G到3G的过渡,移动和联通,sdk1.0 */
				case TelephonyManager.NETWORK_TYPE_EDGE:
					/** (2G) 电信,sdk1.6 */
				case TelephonyManager.NETWORK_TYPE_CDMA:
					/** (2G),sdk1.6 */
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					/** (2G),sdk2.2 */
				case TelephonyManager.NETWORK_TYPE_IDEN:
					return (CURRENT_NETTYPE = NETTYPE_2G);
					/** (3G)联通,sdk1.0 */
				case TelephonyManager.NETWORK_TYPE_UMTS:
					/** (3G)电信,sdk1.6 */
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					/** (3.5G)属于3G过渡,sdk1.6 */
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					/** (3.5G),sdk2.0 */
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					/** (3.5G),sdk2.0 */
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					/** (3G)联通,sdk2.0 */
				case TelephonyManager.NETWORK_TYPE_HSPA:
					/** 3G-3.5G,sdk2.3 */
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
					/** 3G(3G到4G的升级产物),sdk3.0 */
				case TelephonyManager.NETWORK_TYPE_EHRPD:
					/** (3G),sdk3.2 */
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					return (CURRENT_NETTYPE = NETTYPE_3G);
					/** (4G),sdk3.0 */
				case TelephonyManager.NETWORK_TYPE_LTE:
					return (CURRENT_NETTYPE = NETTYPE_4G);
					/** sdk1.0 */
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					return (CURRENT_NETTYPE = NETTYPE_UNKOWN);
				default:
					return (CURRENT_NETTYPE = NETTYPE_UNKOWN);
				}
			}
		} else {
			return (CURRENT_NETTYPE = NETTYPE_UNKOWN);
		}
	}

}
