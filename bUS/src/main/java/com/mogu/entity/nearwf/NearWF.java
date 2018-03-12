package com.mogu.entity.nearwf;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearWF {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("wifi_list1")
    @Expose
    private List<WifiList1> wifiList1 = new ArrayList<WifiList1>();

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
     *     The wifiList1
     */
    public List<WifiList1> getWifiList1() {
        return wifiList1;
    }

    /**
     * 
     * @param wifiList1
     *     The wifi_list1
     */
    public void setWifiList1(List<WifiList1> wifiList1) {
        this.wifiList1 = wifiList1;
    }

}