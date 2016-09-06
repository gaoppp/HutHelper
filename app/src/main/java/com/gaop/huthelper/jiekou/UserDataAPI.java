package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.Electric;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelperdao.User;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/8/2.
 */
public interface UserDataAPI {
    @GET("/api/v1/get/login/{num}/{pass}")
    Observable<HttpResult<User>> getUserData(@Path("num") String num, @Path("pass") String pass);
}
