package com.gaop.huthelper.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 二手市场商品实体类
 * Created by 高沛 on 2016/7/22.
 */
public class Goods {

    /**
     * id : 38
     * tit : 美利达R-903整车出售
     * content : 车子是在河西美利达专卖店买的 有购车时的发票 保修卡 工大同学上门验货 车子 很新买来没骑几次 都在寝室丢着 爽快的买家赠送头盔一个 可小刀
     * prize : 2200
     * prize_src : 2698 原价
     * type : 1
     * class : 4
     * attr : 1
     * pics : ["/uploads/userpics/201606/1467023847.664513573_thumb.jpg"]
     * created_on : 2016-06-27 18:39:43
     * phone :
     * qq : 2025664879
     * wechat :
     * view_cnt : 188
     * pics_src : ["/uploads/userpics/201606/1467023847.664513573.jpg"]
     * user : {"username":"1*******217"}
     */

    private String id;
    private String tit;
    private String content;
    private String prize;
    private String prize_src;
    private String type;
    @SerializedName("class")
    private String classX;
    private String attr;
    private String created_on;
    private String phone;
    private String qq;
    private String wechat;
    private String view_cnt;
    /**
     * username : 1*******217
     */

    private UserBean user;
    private List<String> pics;
    private List<String> pics_src;

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

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getPrize_src() {
        return prize_src;
    }

    public void setPrize_src(String prize_src) {
        this.prize_src = prize_src;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics_src() {
        return pics_src;
    }

    public void setPics_src(List<String> pics_src) {
        this.pics_src = pics_src;
    }

    public static class UserBean {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
