package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/3.
 */
public interface FileUploadAPI {
    /**
     * 上传一张图片
     * @return
     */
    @Multipart
    @POST("api/v1/stuff/upload")
    Observable<HttpResult<String>> uploadImage(@Part MultipartBody.Part file);
}
