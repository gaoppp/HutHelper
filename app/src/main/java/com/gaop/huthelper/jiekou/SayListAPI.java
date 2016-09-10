package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.SayData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-9-10.
 */
public interface SayListAPI {
    @GET("api/v1/moments/posts/{num}")
    Observable<HttpResult<SayData>> getSayList(@Path("num") int num);
}
