package com.gaop.huthelper.model.network.api;

import com.gaop.huthelper.model.entity.OfferData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by gaop on 16-11-14.
 */

public interface OfferAPI {
    /**
     * 获取token
     * @param appid
     * @param appsecret
     * @return
     */
    @FormUrlEncoded
    @POST("http://www.ioffershow.com:8000/webapi/gettoken/")
    Observable<OfferData> getToken(@Field("appid") String appid, @Field("appsecret") String appsecret);

    /**
     * 获取全部offer
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("http://www.ioffershow.com:8000/webapi/jobtotal/")
    Observable<OfferData> getOfferList(@Field("access_token") String token);

    /**
     * 查找Offer
     * @param token
     * @param content 查找内容
     * @return
     */
    @FormUrlEncoded
    @POST("http://www.ioffershow.com:8000/webapi/jobsearch/")
    Observable<OfferData> searchOffer(@Field("access_token") String token,@Field("content") String content);


}
