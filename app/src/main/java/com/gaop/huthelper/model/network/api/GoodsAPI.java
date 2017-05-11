package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.Goods;
import com.gaop.huthelper.model.entity.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/2.
 */
public interface GoodsAPI {

    @GET("/api/v1/stuff/detail/{num}/{rember_code}/{id}")
    Observable<HttpResult<Goods>> getGoodsContent(@Path("num") String num, @Path("rember_code") String rember_code, @Path("id") String id);

}
