package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.SayData;

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
public interface SayAPI {
    /**
     * 分页获取说说
     * @param num
     * @return
     */

    @GET("api/v1/moments/posts/{num}")
    Observable<HttpResult<SayData>> getSayList(@Path("num") int num);
    /**
     * 获取指定id说说
     * @param user_id
     * @return
     */
    @GET("/api/v1/moments/posts/page/{user_id}")
    Observable<HttpResult<SayData>> getSayListById(@Path("user_id") String user_id);

    /**
     * 删除说说
     * @param num
     * @param rember_code
     * @param id
     * @return
     */

    @GET("/api/v1/moments/delete/{num}/{rember_code}/{id}")
    Observable<HttpResult> deleteSay(@Path("num") String num, @Path("rember_code") String rember_code, @Path("id") String id);


    /**
     * 发布说说
     * @param num
     * @param rember_code
     * @param content
     * @param hidden
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/moments/create/{num}/{rember_code}")
    Observable<HttpResult<String>> addSay(@Path("num") String num, @Path("rember_code") String rember_code,
                                          @Field("content") String content, @Field("hidden") String hidden);

    /**
     * 上传一张图片
     * @return
     */
    @Multipart
    @POST("api/v1/moments/upload")
    Observable<HttpResult<String>> uploadImage(@Part MultipartBody.Part file);


    /**
     * 发布评论
     * @param num
     * @param code
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/moments/comment/{num}/{code}/{id}")
    Observable<HttpResult> addComment(@Path("num") String num,@Path("code") String code,@Path("id") String id,@Field("comment") String comment);
}
