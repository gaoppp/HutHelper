package com.gaop.huthelper.model.entity;

import java.util.List;

/**
 * Created by gaop on 16-11-14.
 */

public class OfferData {

    /**
     * access_token : pbkdf2_sha256$15000$Pt8mXzNFiIaW$ccF2fC9dv11li9XYA7RU3jiaR4eXxcggzRp52fymQfA=
     * msg : 成功获取access_token
     * r : 1
     */

    private String access_token;
    private String msg;
    private int r;

    private List<OfferInfo> info;

    public List<OfferInfo> getInfo() {
        return info;
    }

    public void setInfo(List<OfferInfo> info) {
        this.info = info;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
