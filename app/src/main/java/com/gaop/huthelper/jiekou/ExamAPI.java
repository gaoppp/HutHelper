package com.gaop.huthelper.jiekou;

import com.gaop.huthelper.Model.ExamData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaop on 16-10-16.
 */

public interface ExamAPI {
    @GET("http://218.75.197.124:84/api/exam/{num}/key/{md5}")
    Observable<ExamData> getExamData(@Path("num") String num, @Path("md5") String md5);
}
