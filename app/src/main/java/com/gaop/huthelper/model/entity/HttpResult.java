package com.gaop.huthelper.model.entity;

/**
 * Created by 高沛 on 2016/8/29.
 * 网络数据
 */
public class HttpResult<T> {
    private String msg;
    private String remember_code_app;
    private String code;

    private T data;
    private int test;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRemember_code_app() {
        return remember_code_app;
    }

    public void setRemember_code_app(String remember_code_app) {
        this.remember_code_app = remember_code_app;
    }
}
