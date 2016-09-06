package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/26.
 */
public interface DeleteGoodAPI {
    @GET("/api/v1/stuff/delete/{num}/{rember_code}/{id}")
    Observable<HttpResult> deleteGoods(@Path("num") String num,@Path("rember_code") String rember_code,@Path("id") String id);
}
