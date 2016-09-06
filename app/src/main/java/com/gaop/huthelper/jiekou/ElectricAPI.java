package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.Electric;



import retrofit2.Call;
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
