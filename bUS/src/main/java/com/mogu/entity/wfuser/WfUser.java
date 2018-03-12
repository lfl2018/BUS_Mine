package com.mogu.entity.wfuser;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WfUser {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("friends_list")
    @Expose
    private List<FriendsList> friendsList = new ArrayList<FriendsList>();
    @Expose
    private Pageinfo pageinfo;

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
     *     The friendsList
     */
    public List<FriendsList> getFriendsList() {
        return friendsList;
    }

    /**
     * 
     * @param friendsList
     *     The friends_list
     */
    public void setFriendsList(List<FriendsList> friendsList) {
        this.friendsList = friendsList;
    }

    /**
     * 
     * @return
     *     The pageinfo
     */
    public Pageinfo getPageinfo() {
        return pageinfo;
    }

    /**
     * 
     * @param pageinfo
     *     The pageinfo
     */
    public void setPageinfo(Pageinfo pageinfo) {
        this.pageinfo = pageinfo;
    }

}
