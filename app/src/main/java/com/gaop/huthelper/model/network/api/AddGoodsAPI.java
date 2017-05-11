package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/5.
 */
public interface AddGoodsAPI {
    @FormUrlEncoded
    @POST("api/v1/stuff/create/{num}/{rember_code}")
    Observable<HttpResult<String>> AddGoods(@Path("num") String num, @Path("rember_code") String rember_code,
                                            @Field("tit") String tit, @Field("content") String content,
                                            @Field("prize") String price, @Field("prize_src") String price_src,
                                            @Field("class") int Class,
                                            @Field("attr") int attr, @Field("hidden") String hidden,
                                            @Field("phone") String phone, @Field("qq") String qq, @Field("wechat") String wechat);
}
