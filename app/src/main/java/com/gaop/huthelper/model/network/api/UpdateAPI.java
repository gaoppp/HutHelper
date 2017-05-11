package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.UpdateMsg;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/1.
 */
public interface UpdateAPI {
    @GET("api/v1/get/version/{num}/{version}")
    Observable<HttpResult<UpdateMsg>> getUpdateData(@Path("num") String num,@Path("version") String version);
}
