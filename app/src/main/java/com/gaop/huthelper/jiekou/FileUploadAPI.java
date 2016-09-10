package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.HttpResult;

import java.io.File;
import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
