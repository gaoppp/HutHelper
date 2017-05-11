package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.WeatherData;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by gaop on 16-11-21.
 */

public interface WeatherAPI {
    @GET("http://op.juhe.cn/onebox/weather/query?cityname=%e6%a0%aa%e6%b4%b2&key=6fcf23baa6f612da38bebcdcf5436366")
    Observable<WeatherData> getWeather();

}
