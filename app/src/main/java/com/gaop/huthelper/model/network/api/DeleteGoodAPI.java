package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/26.
 */
public interface DeleteGoodAPI {
    /**
     * 删除商品
     * @param num 学号
     * @param rember_code 校验码
     * @param id 商品id
     * @return
     */
    @GET("/api/v1/stuff/delete/{num}/{rember_code}/{id}")
    Observable<HttpResult> deleteGoods(@Path("num") String num,@Path("rember_code") String rember_code,@Path("id") String id);
}
