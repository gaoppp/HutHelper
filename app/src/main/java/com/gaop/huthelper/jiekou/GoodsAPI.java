package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.Goods;
import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;

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
