package com.gaop.huthelper.model.network.api;


import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelperdao.CourseGrade;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop1 on 2016/7/30.
 */
public interface GradeAPI {
    @GET("api/v1/get/scores/{xh}/{code_app}/{sha1}")
    Observable<HttpResult<List<CourseGrade>>> getGrade(@Path("xh") String xh, @Path("code_app") String code_app
            , @Path("sha1") String sha1);
}
