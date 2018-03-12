package com.mogu.entity.friend;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendJson {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("friend_list")
    @Expose
    private List<FriendList> friendList = new ArrayList<FriendList>();

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
     *     The friendList
     */
    public List<FriendList> getFriendList() {
        return friendList;
    }

    /**
     * 
     * @param friendList
     *     The friend_list
     */
    public void setFriendList(List<FriendList> friendList) {
        this.friendList = friendList;
    }

}
