package com.gaop.huthelper.Model;

/**
 * Created by gaop1 on 2016/8/29.
 */
public class HttpResult<T> {
    private String msg;
    private String remember_code_app;
    private T data;
    private int test;

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
