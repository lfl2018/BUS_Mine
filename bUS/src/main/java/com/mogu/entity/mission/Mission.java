package com.mogu.entity.mission;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mission {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("mrrw_list")
    @Expose
    private List<MrrwList> mrrwList = new ArrayList<MrrwList>();

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
     *     The mrrwList
     */
    public List<MrrwList> getMrrwList() {
        return mrrwList;
    }

    /**
     * 
     * @param mrrwList
     *     The mrrw_list
     */
    public void setMrrwList(List<MrrwList> mrrwList) {
        this.mrrwList = mrrwList;
    }

}
