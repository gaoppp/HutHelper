package com.gaop.huthelper.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 失物招领实体类
 * Created by 高沛 on 16-11-10.
 */

public class Lose implements Serializable{

    /**
     * id : 11
     * tit : 眼镜
     * content : 哪位宝宝在公共楼309 丢失了眼镜请来联系认领 电话：18229704132 qq:769841753
     * locate : 公共楼309
     * time : 2016-11-09 00:00:00
     * user_id : 16570
     * pics : ["/uploads/moments/201611/1478666424.701551318_thumb.png","/uploads/moments/201611/1478666425.748433491_thumb.png","/uploads/moments/201611/1478666427.01465420_thumb.png"]
     * created_on : 2016-11-09
     * phone : 18229704132
     * qq :
     * wechat :
     * username : root
     * dep_name : 计算机与通信学院
     */

    private String id;
    private String tit;
    private String content;
    private String locate;
    private String time;
    private String user_id;
    private String created_on;
    private String phone;
    private String qq;
    private String wechat;
    private String username;
    private String dep_name;
    private List<String> pics;

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

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    @Override
    public String toString() {
        return tit+username+locate+content;
    }
}
