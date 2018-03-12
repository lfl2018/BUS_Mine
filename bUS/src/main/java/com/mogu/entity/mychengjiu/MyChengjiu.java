package com.mogu.entity.mychengjiu;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyChengjiu {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("cj_list")
    @Expose
    private List<CjList> cjList = new ArrayList<CjList>();

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
     *     The cjList
     */
    public List<CjList> getCjList() {
        return cjList;
    }

    /**
     * 
     * @param cjList
     *     The cj_list
     */
    public void setCjList(List<CjList> cjList) {
        this.cjList = cjList;
    }

}
