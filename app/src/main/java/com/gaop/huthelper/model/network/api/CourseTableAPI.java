package com.gaop.huthelper.model.network.api;


import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelperdao.Lesson;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 高沛 on 2016/7/28.
 * 课程表API
 */
public interface CourseTableAPI {
    @GET("/api/v1/get/lessons/{xh}/{code_app}")
    Observable<HttpResult<List<Lesson>>> getCourseTable(@Path("xh") String xh, @Path("code_app") String code_app);
}
