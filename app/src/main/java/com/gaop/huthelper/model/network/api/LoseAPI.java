package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.model.entity.PageData;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-9-10.
 */
public interface LoseAPI {
    /**
     * 分页获取失物招领
     * @param num
     * @return
     */
    @GET("api/v1/loses/posts/{num}")
    Observable<HttpResult<PageData<Lose>>> getLosesList(@Path("num") int num);

    /**
     * 获取指定id失物招领
     * @param user_id 用户id
     * @return
     */
    @GET("/api/v1/loses/posts/page/{user_id}")
    Observable<HttpResult<PageData<Lose>>> getLoseListById(@Path("user_id") String user_id);

    /**
     * 删除
     * @param num
     * @param rember_code
     * @param id 失物id
     * @return
     */
    @GET("/api/v1/loses/delete/{num}/{rember_code}/{id}")
    Observable<HttpResult> deleteLose(@Path("num") String num, @Path("rember_code") String rember_code, @Path("id") String id);

    /**
     * 发布失物
     * @param num
     * @param rember_code
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/loses/create/{num}/{rember_code}")
    Observable<HttpResult<String>> addLose(@Path("num") String num, @Path("rember_code") String rember_code,
                                           @Field("tit") String tit,@Field("locate") String locate, @Field("time") String time,
                                           @Field("content") String content, @Field("hidden") String hidden,
                                           @Field("phone") String phone, @Field("qq") String qq, @Field("wechat") String wechat);

    /**
     * 上传一张图片
     * @return
     */
    @Multipart
    @POST("api/v1/loses/upload")
    Observable<HttpResult<String>> uploadImage(@Part MultipartBody.Part file);


}
