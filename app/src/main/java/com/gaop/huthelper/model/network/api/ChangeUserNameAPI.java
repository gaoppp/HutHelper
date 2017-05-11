package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-10-6.
 */

public interface ChangeUserNameAPI {
    @FormUrlEncoded
    @POST("api/v1/set/profile/{num}/{code}")
    Observable<HttpResult> changeUsername(@Path("num") String num, @Path("code") String code,
                                          @Field("username") String name);
}
