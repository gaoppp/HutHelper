package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-9-10.
 */
public interface  AddSayLikeAPI {

    @GET("api/v1/moments/like/{num}/{code}/{id}")
    Observable<HttpResult> likeSay(@Path("num") String num,@Path("code") String code,@Path("id") String id);

    @GET("api/v1/moments/like/{num}/{code}/")
    Observable<HttpResult<List<String>>> getAllLikeSay(@Path("num") String num, @Path("code") String code);



}
