package com.gaop.huthelper.Model;

/**
 * Created by gaop1 on 2016/9/1.
 */
public class UpdateMsg {
    /**
     * versionNum : 1
     * url :
     * data : test
     */

    private int versionNum;
    private String url;
    private String data;

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
