package com.gaop.huthelper.model.entity;

import java.io.Serializable;

/**
 * 校招薪水实体类
 * Created by 高沛 on 16-11-14.
 */

public class OfferInfo implements Serializable{


    /**
     * salary : 14k*14-16
     * city : 杭州
     * remark : 外加配股
     * company : 恒生电子
     * number : 71
     * score : 2
     * time : 2016-11-13 22:53:43
     * position : 软件开发
     * id : 656
     */

    private String salary;
    private String city;
    private String remark;
    private String company;
    private int number;
    private int score;
    private String time;
    private String position;
    private int id;

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
