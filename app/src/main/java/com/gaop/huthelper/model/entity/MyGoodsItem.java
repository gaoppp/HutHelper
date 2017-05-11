package com.gaop.huthelper.model.entity;

/**
 * 我的失物招领
 * Created by 高沛 on 2016/9/4.
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
    private String title;
    private String content;
    private String created_on;
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
