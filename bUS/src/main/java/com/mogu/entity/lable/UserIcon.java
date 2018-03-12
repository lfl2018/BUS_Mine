package com.mogu.entity.lable;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;

public class UserIcon {

    @Expose
    private String code;
    @Expose
    private String msg;
    @Expose
    private java.util.List<Iconlist> list = new ArrayList<Iconlist>();

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
     *     The list
     */
    public java.util.List<Iconlist> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<Iconlist> list) {
        this.list = list;
    }

}
