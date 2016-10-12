package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-10-6.
 */

public interface ChangeUserNameAPI {
    @GET("api/v1/set/profile/{num}/{code}/username/{name}")
    Observable<HttpResult> changeUsername(@Path("num") String num, @Path("code") String code, @Path("name") String name);
}
