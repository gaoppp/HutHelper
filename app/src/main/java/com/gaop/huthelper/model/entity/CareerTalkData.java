package com.gaop.huthelper.model.entity;

/**
 * Created by gaop1 on 2017/4/9.
 */

public class CareerTalkData<T> {
    /**
     * status : success
     * data : []
     * topAdCount : 0
     */

    private String status;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
