package com.mogu.entity.login;

import com.google.gson.annotations.Expose;

public class LoginJson {

	@Expose
	private String code;
	@Expose
	private String msg;
	@Expose
	private Login AjaxUserDTO;

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
	 * @return The AjaxUserDTO
	 */
	public Login getAjaxUserDTO() {
		return AjaxUserDTO;
	}

	/**
	 * 
	 * @param AjaxUserDTO
	 *            The AjaxUserDTO
	 */
	public void setAjaxUserDTO(Login AjaxUserDTO) {
		this.AjaxUserDTO = AjaxUserDTO;
	}

}
