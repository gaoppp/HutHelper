package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.Electric;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/22.
 */
public interface ElectricAPI {

        @GET("api/v1/get/power/{lou}/{hao}")
        Observable<Electric> electricData(@Path("lou") String lou,@Path("hao") String hao);
}
