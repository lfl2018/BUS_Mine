package com.mogu.entity.login;

import com.google.gson.annotations.Expose;

public class GetUserJson {

	@Expose
	private String code;
	@Expose
	private String msg;
	@Expose
	private Login UserHyb000;

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
	 * @return The UserHyb000
	 */
	public Login getUserHyb000() {
		return UserHyb000;
	}

	/**
	 * 
	 * @param UserHyb000
	 *            The UserHyb000
	 */
	public void setUserHyb000(Login UserHyb000) {
		this.UserHyb000 = UserHyb000;
	}

}
