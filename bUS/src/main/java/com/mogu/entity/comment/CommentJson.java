package com.mogu.entity.comment;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentJson {

	@Expose
	private String code;
	@Expose
	private String msg;
	@SerializedName("huati_list")
	@Expose
	private List<Comment> huatiList = new ArrayList<Comment>();

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
	 * @return The huatiList
	 */
	public List<Comment> getHuatiList() {
		return huatiList;
	}

	/**
	 * 
	 * @param huatiList
	 *            The huati_list
	 */
	public void setHuatiList(List<Comment> huatiList) {
		this.huatiList = huatiList;
	}

}
