package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.DepInfo;


import java.util.List;

import retrofit2.http.GET;

import rx.Observable;

/**
 * Created by gaop1 on 2016/8/2.
 */
public interface AllClassAPI {
    @GET("api/get/classAll")
    Observable<List<DepInfo>> getAllClass();
}
