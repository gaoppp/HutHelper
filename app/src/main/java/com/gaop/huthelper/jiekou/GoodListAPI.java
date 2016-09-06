package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.GoodsListItem;

import org.json.JSONArray;


import java.lang.reflect.Array;
import java.util.Arrays;

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
