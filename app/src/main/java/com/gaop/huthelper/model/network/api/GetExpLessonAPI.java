package com.gaop.huthelper.model.network.api;


import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelperdao.Explesson;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-10-9.
 */

public interface GetExpLessonAPI {
    @GET("api/v1/get/lessonsexp/{xh}/{code_app}")
    Observable<HttpResult<List<Explesson>>> getExpLesson(@Path("xh") String xh, @Path("code_app") String code_app);
}
