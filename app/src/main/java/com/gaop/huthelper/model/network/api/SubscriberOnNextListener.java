package com.gaop.huthelper.model.network.api;

/**
 * Created by 高沛 on 2016/7/24.
 */
public interface SubscriberOnNextListener<T>{
    void onNext( T t);
    //void onError(Throwable e);
}
