package com.mogu.entity.topiclist;

import java.io.Serializable;

import android.R.integer;

import com.google.gson.annotations.Expose;

public class HuatiList implements Serializable {

	@Expose
	private String biaoti;
	@Expose
	private String fbriqi;
	@Expose
	private String huatbh;
	@Expose
	private String hybh00;
	@Expose
	private String nrong0;
	@Expose
	private String xxlxin;
	@Expose
	private Integer hzshu0;
	@Expose
	private Integer plshu0;

	public Integer getHzshu0() {
		return hzshu0;
	}

	public void setHzshu0(Integer hzshu0) {
		this.hzshu0 = hzshu0;
	}

	public Integer getPlshu0() {
		return plshu0;
	}

	public void setPlshu0(Integer plshu0) {
		this.plshu0 = plshu0;
	}

	/**
	 * 
	 * @return The biaoti
	 */
	public String getBiaoti() {
		return biaoti;
	}

	/**
	 * 
	 * @param biaoti
	 *            The biaoti
	 */
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}

	/**
	 * 
	 * @return The fbriqi
	 */
	public String getFbriqi() {
		return fbriqi;
	}

	/**
	 * 
	 * @param fbriqi
	 *            The fbriqi
	 */
	public void setFbriqi(String fbriqi) {
		this.fbriqi = fbriqi;
	}

	/**
	 * 
	 * @return The huatbh
	 */
	public String getHuatbh() {
		return huatbh;
	}

	/**
	 * 
	 * @param huatbh
	 *            The huatbh
	 */
	public void setHuatbh(String huatbh) {
		this.huatbh = huatbh;
	}

	/**
	 * 
	 * @return The hybh00
	 */
	public String getHybh00() {
		return hybh00;
	}

	/**
	 * 
	 * @param hybh00
	 *            The hybh00
	 */
	public void setHybh00(String hybh00) {
		this.hybh00 = hybh00;
	}

	/**
	 * 
	 * @return The nrong0
	 */
	public String getNrong0() {
		return nrong0;
	}

	/**
	 * 
	 * @param nrong0
	 *            The nrong0
	 */
	public void setNrong0(String nrong0) {
		this.nrong0 = nrong0;
	}

	/**
	 * 
	 * @return The xxlxin
	 */
	public String getXxlxin() {
		return xxlxin;
	}

	/**
	 * 
	 * @param xxlxin
	 *            The xxlxin
	 */
	public void setXxlxin(String xxlxin) {
		this.xxlxin = xxlxin;
	}

}
