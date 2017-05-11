package com.gaop.huthelper.model.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/26.
 */
public interface MyGoodListAPI {
    /**
     * 我的商品
     * @param num 学号
     * @param rember_code 校验码
     * @return
     */
    @GET("api/v1/stuff/own1/{num}/{rember_code}/1")
    Observable<ResponseBody> getMyGoodsList(@Path("num") String num, @Path("rember_code") String rember_code);
}
