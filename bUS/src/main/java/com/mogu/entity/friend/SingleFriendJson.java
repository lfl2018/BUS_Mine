package com.mogu.entity.friend;

import com.google.gson.annotations.Expose;

public class SingleFriendJson {

    @Expose
    private String code;
    @Expose
    private String msg;
    @Expose
    private Friend UserHyb000;

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
     *     The UserHyb000
     */
    public Friend getUserHyb000() {
        return UserHyb000;
    }

    /**
     * 
     * @param UserHyb000
     *     The UserHyb000
     */
    public void setUserHyb000(Friend UserHyb000) {
        this.UserHyb000 = UserHyb000;
    }

}
