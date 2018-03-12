package com.mogu.entity.topiclist;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicList {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("huati_list")
    @Expose
    private List<HuatiList> huatiList = new ArrayList<HuatiList>();

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
     *     The huatiList
     */
    public List<HuatiList> getHuatiList() {
        return huatiList;
    }

    /**
     * 
     * @param huatiList
     *     The huati_list
     */
    public void setHuatiList(List<HuatiList> huatiList) {
        this.huatiList = huatiList;
    }

}
