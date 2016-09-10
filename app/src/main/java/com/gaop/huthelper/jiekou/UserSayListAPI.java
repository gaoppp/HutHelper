package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.Say;
import com.gaop.huthelper.Model.SayData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-9-10.
 */
public interface UserSayListAPI {
    @GET("/api/v1/moments/posts/page/{user_id}")
    Observable<HttpResult<SayData>> getSayList(@Path("user_id") String user_id);
}
