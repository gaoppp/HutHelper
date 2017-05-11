package com.gaop.huthelper.net;

/**
 * Created by 高沛 on 2017/4/18.
 */

public interface RequestCallBack<T> {
    void start(T data);

    void requestSuccess(T data);

    void requestFail(Throwable e);

    void onCompleted();
}
