package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.UpdateMsg;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/1.
 */
public interface UpdateAPI {
    @GET("api/v1/get/version")
    Observable<HttpResult<UpdateMsg>> getUpdateData();
}
