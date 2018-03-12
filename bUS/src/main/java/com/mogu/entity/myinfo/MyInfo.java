package com.mogu.entity.myinfo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class MyInfo {

    @Expose
    private String code;
    @Expose
    private String msg;
    @Expose
    private FriendList UserHyb000;
    @Expose
    private List<String> bqlist = new ArrayList<String>();

    public List<String> getBqlist() {
		return bqlist;
	}

	public void setBqlist(List<String> bqlist) {
		this.bqlist = bqlist;
	}

	/**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 
     * @param msg
     *     The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 
     * @return
     *     The UserHyb000
     */
    public FriendList getUserHyb000() {
        return UserHyb000;
    }

    /**
     * 
     * @param UserHyb000
     *     The UserHyb000
     */
    public void setUserHyb000(FriendList UserHyb000) {
        this.UserHyb000 = UserHyb000;
    }

}
