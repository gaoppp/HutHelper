package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.GoodsListItem;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/26.
 */
public interface GoodListAPI {
    @GET("api/v1/stuff/goods/{num}")
    Observable<GoodsListItem[]> getGoodsList(@Path("num") int num);
}
