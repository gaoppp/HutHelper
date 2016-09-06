package com.gaop.huthelper.Model;

/**
 * Created by gaop1 on 2016/9/4.
 */
public class MyGoodsItem {

    /**
     * id : 66
     * tit : 123
     * content : 1213
     * created_on : 2016-09-04 19:12:40
     * view_cnt : 2
     */

    private String id;
    private String tit;
    private String content;
    private String created_on;
    private String view_cnt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }
}
