package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.RankingApiData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2017/4/27.
 */

public interface RankingDataAPI {
    @GET("/api/v1/get/ranking/{num}/{code}")
    Observable<RankingApiData> getRankingData(@Path("num") String xh, @Path("code") String code);
}
