package com.gaop.huthelper.jiekou;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by gaop1 on 2016/9/3.
 */
public interface FileUploadAPI {
    /**
     * 上传一张图片
     * @param description
     * @param imgs
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs);


    /**
     * 上传5张图片
     * @param description
     * @param imgs1
     * @param imgs2
     * @param imgs3
     * @param imgs4
     * @param imgs5
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("description") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs2,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs3,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs4,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs5);

    /**
     * 简便写法
     * @param description
     * @param imgs1
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("description") String description, @PartMap
    Map<String, RequestBody> imgs1);
}
