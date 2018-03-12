package com.mogu.entity.mywifilist;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyWifiList {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("wifi_list1")
    @Expose
    private List<WifiList1> wifiList1 = new ArrayList<WifiList1>();
    @SerializedName("wifi_list2")
    @Expose
    private List<WifiList2> wifiList2 = new ArrayList<WifiList2>();

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

    /**
     * 
     * @return
     *     The wifiList2
     */
    public List<WifiList2> getWifiList2() {
        return wifiList2;
    }

    /**
     * 
     * @param wifiList2
     *     The wifi_list2
     */
    public void setWifiList2(List<WifiList2> wifiList2) {
        this.wifiList2 = wifiList2;
    }

}
