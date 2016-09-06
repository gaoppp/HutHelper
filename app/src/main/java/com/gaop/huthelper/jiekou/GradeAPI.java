package com.gaop.huthelper.jiekou;


import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Grade;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by gaop1 on 2016/7/30.
 */
public interface GradeAPI {
    @GET("api/v1/get/scores/{xh}/{code_app}")
    Observable<HttpResult<List<CourseGrade>>> getGrade(@Path("xh") String xh, @Path("code_app") String code_app);
}
