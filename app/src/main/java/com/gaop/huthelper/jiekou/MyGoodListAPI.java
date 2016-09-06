package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.MyGoodsItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/26.
 */
public interface MyGoodListAPI {
    @GET("api/v1/stuff/own/{num}/{rember_code}/1")
    Observable<HttpResult<List<MyGoodsItem>>> getMyGoodsList(@Path("num") String num,@Path("rember_code") String rember_code);
}
