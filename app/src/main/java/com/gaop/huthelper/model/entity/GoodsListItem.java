package com.gaop.huthelper.model.entity;

/**
 * Created by 高沛 on 2016/7/26.
 */
public class GoodsListItem {


    /**
     * image : /uploads/userpics/201606/1466433620.016541648_thumb.JPG
     * title : Launchpads
     * id : 14
     * prize : 1,200.00
     * created_on : 35天前
     */

    private String image;
    private String title;
    private String id;
    private String prize;
    private String created_on;
    /**
     * page_max : 2
     * page_cur : 2
     */

    private int page_max;
    private String page_cur;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public int getPage_max() {
        return page_max;
    }

    public void setPage_max(int page_max) {
        this.page_max = page_max;
    }

    public String getPage_cur() {
        return page_cur;
    }

    public void setPage_cur(String page_cur) {
        this.page_cur = page_cur;
    }
}
