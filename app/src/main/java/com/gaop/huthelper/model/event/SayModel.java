package com.gaop.huthelper.model.event;

import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.RequestCallBack;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import rx.Subscriber;

/**
 * Created by gaop1 on 2017/4/18.
 */

public interface SayModel {

    void request(int page, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);

    void likeSay(String sayId, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);

    void commitSay(String id, String commit, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);

    void loadLikedSays(LifecycleTransformer lifecycleTransformer, Subscriber<HttpResult<List<String>>> subscriber);

    void deleteSay(String id, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);

}
