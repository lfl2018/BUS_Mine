package com.mogu.entity.update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

	@Expose
	private String code;
	@Expose
	private String msg;
	@SerializedName("android_ver")
	@Expose
	private String androidVer;
	@SerializedName("android_ver_flag")
	@Expose
	private String androidVerFlag;
	@SerializedName("android_up_content")
	@Expose
	private String androidUpContent;
	@SerializedName("android_auto_up")
	@Expose
	private String androidAutoUp;

	/**
	 * 
	 * @return The code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 *            The code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return The msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 
	 * @param msg
	 *            The msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 
	 * @return The androidVer
	 */
	public String getAndroidVer() {
		return androidVer;
	}

	/**
	 * 
	 * @param androidVer
	 *            The android_ver
	 */
	public void setAndroidVer(String androidVer) {
		this.androidVer = androidVer;
	}

	/**
	 * 
	 * @return The androidVerFlag
	 */
	public String getAndroidVerFlag() {
		return androidVerFlag;
	}

	/**
	 * 
	 * @param androidVerFlag
	 *            The android_ver_flag
	 */
	public void setAndroidVerFlag(String androidVerFlag) {
		this.androidVerFlag = androidVerFlag;
	}

	/**
	 * 
	 * @return The androidUpContent
	 */
	public String getAndroidUpContent() {
		return androidUpContent;
	}

	/**
	 * 
	 * @param androidUpContent
	 *            The android_up_content
	 */
	public void setAndroidUpContent(String androidUpContent) {
		this.androidUpContent = androidUpContent;
	}

	/**
	 * 
	 * @return The androidAutoUp
	 */
	public String getAndroidAutoUp() {
		return androidAutoUp;
	}

	/**
	 * 
	 * @param androidAutoUp
	 *            The android_auto_up
	 */
	public void setAndroidAutoUp(String androidAutoUp) {
		this.androidAutoUp = androidAutoUp;
	}

}
