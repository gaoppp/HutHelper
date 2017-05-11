package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.CareerTalk;
import com.gaop.huthelper.model.entity.CareerTalkData;
import com.gaop.huthelper.model.entity.CareerTalkItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 高沛 on 2017/4/21.
 */

public interface CareerTalkAPI {

    @GET("http://api.haitou.cc/xjh/list")
    Observable<CareerTalkData<List<CareerTalk>>> getCareerTalkList(@Query("kind") String kind, @Query("zone") String zone, @Query("page") int page);

    @GET("http://api.haitou.cc/xjh/view")
    Observable<CareerTalkData<CareerTalkItem>> getCareerTalk(@Query("id") String id);

    @GET("http://api.haitou.cc/xjh/list?zone=cs&key={key}&page={page}")
    Observable<CareerTalkData<List<CareerTalk>>> searchCareerTalk(@Path("key") String key, @Path("page") int page);

    @GET("http://api.haitou.cc/xjh/list")
    Observable<CareerTalkData<List<CareerTalk>>> getCareerTalkList(@Query("univ_id") int univId, @Query("kind") String kind, @Query("zone") String zone, @Query("page") int page);


}
