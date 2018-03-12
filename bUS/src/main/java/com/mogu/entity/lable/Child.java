package com.mogu.entity.lable;

import com.google.gson.annotations.Expose;

public class Child {

    @Expose
    private String icbh00;
    @Expose
    private String icname;
    @Expose
    private String parent;

    /**
     * 
     * @return
     *     The icbh00
     */
    public String getIcbh00() {
        return icbh00;
    }

    /**
     * 
     * @param icbh00
     *     The icbh00
     */
    public void setIcbh00(String icbh00) {
        this.icbh00 = icbh00;
    }

    /**
     * 
     * @return
     *     The icname
     */
    public String getIcname() {
        return icname;
    }

    /**
     * 
     * @param icname
     *     The icname
     */
    public void setIcname(String icname) {
        this.icname = icname;
    }

    /**
     * 
     * @return
     *     The parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * 
     * @param parent
     *     The parent
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

}
