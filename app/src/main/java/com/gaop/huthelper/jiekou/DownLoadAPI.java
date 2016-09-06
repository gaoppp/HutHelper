package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.net.ProgressResponseBody;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by gaop1 on 2016/9/1.
 */
public interface DownLoadAPI {
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
