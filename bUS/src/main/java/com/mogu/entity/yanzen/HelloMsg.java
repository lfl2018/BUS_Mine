package com.mogu.entity.yanzen;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelloMsg {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("msg_list")
    @Expose
    private List<MsgList> msgList = new ArrayList<MsgList>();

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
     *     The msgList
     */
    public List<MsgList> getMsgList() {
        return msgList;
    }

    /**
     * 
     * @param msgList
     *     The msg_list
     */
    public void setMsgList(List<MsgList> msgList) {
        this.msgList = msgList;
    }

}
