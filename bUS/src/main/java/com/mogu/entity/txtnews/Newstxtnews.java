package com.mogu.entity.txtnews;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Newstxtnews {

    @Expose
    private String code;
    @Expose
    private String msg;
    @SerializedName("news_list")
    @Expose
    private List<NewsList> newsList = new ArrayList<NewsList>();

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
     *     The newsList
     */
    public List<NewsList> getNewsList() {
        return newsList;
    }

    /**
     * 
     * @param newsList
     *     The news_list
     */
    public void setNewsList(List<NewsList> newsList) {
        this.newsList = newsList;
    }

}
