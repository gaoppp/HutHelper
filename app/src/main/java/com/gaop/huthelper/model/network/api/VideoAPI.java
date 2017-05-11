package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.VideoData;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by gaop1 on 2017/4/9.
 */

public interface VideoAPI {
    /**
     * 获取视频数据
     * @return
     */
    @GET("http://vedio.wxz.name/api/vedio.html")
    Observable<VideoData> getVideoData();
}
