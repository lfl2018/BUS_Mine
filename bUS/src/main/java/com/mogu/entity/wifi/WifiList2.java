package com.mogu.entity.wifi;

import com.google.gson.annotations.Expose;

public class WifiList2 {

	@Expose
	private String ssid00;
	@Expose
	private String mac000;

	/**
	 * 
	 * @return The ssid00
	 */
	public String getSsid00() {
		return ssid00;
	}

	/**
	 * 
	 * @param ssid00
	 *            The ssid00
	 */
	public void setSsid00(String ssid00) {
		this.ssid00 = ssid00;
	}

	/**
	 * 
	 * @return The mac000
	 */
	public String getMac000() {
		return mac000;
	}

	/**
	 * 
	 * @param mac000
	 *            The mac000
	 */
	public void setMac000(String mac000) {
		this.mac000 = mac000;
	}

}
