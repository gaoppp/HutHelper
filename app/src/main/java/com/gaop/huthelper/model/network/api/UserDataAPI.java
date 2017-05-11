package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelperdao.User;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/8/2.
 */
public interface UserDataAPI {
    /**
     * 登录
     * @param num 学号
     * @param pass 密码
     * @return
     */
    @GET("/api/v1/get/login/{num}/{pass}")
    Observable<HttpResult<User>> getUserData(@Path("num") String num, @Path("pass") String pass);

    /**
     * 修改头像
     * @param num 学号
     * @param code 校验码
     * @param file 头像文件
     * @return
     */
    @Multipart
    @POST("/api/v1/set/avatar/{num}/{code}")
    Observable<HttpResult<String>> uploadAvatar(@Path("num") String num,@Path("code")String code,
                                                @Part MultipartBody.Part file);
}
