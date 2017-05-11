package com.gaop.huthelper.model.impl;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.event.SayModel;
import com.gaop.huthelper.model.network.api.AddSayLikeAPI;
import com.gaop.huthelper.model.network.api.SayAPI;
import com.gaop.huthelper.model.network.exception.DataException;
import com.gaop.huthelper.model.network.exception.UserLoginException;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.RequestCallBack;
import com.gaop.huthelperdao.User;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 说说Model实现类
 * Created by gaop1 on 2017/4/18.
 */

public class SayModelImpl implements SayModel {

    private User user;

    public SayModelImpl() {
        user = DBHelper.getUserDao().get(0);
    }

    /**
     * 请求数据实现
     * @param page 页码
     * @param lifecycleTransformer
     * @param callBack 回调接口
     */
    @Override
    public void request(int page, LifecycleTransformer lifecycleTransformer, final RequestCallBack callBack) {
        SayAPI sayAPI = HttpMethods.getRetrofit().create(SayAPI.class);
        sayAPI.getSayList(page)
                .compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<PageData<Say>>>() {
                    @Override
                    public void onCompleted() {
                        callBack.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestFail(e);
                    }

                    @Override
                    public void onNext(HttpResult<PageData<Say>> says) {
                        //dataDB.insert(dataEntitiesList);
                        callBack.requestSuccess(says);
                    }

                });
    }

    @Override
    public void likeSay(String sayId, LifecycleTransformer lifecycleTransformer, final RequestCallBack callBack) {
        // 暂时实现在Adapter中 方便与我的说说界面重用
    }

    /**
     * 评论发布实现 其中可能会抛出 UserLoginException(异地登录异常） DataException(其他数据错误）
     * @param id 说说id
     * @param commit 内容
     * @param lifecycleTransformer
     * @param callBack
     */
    @Override
    public void commitSay(String id, String commit, LifecycleTransformer lifecycleTransformer, final RequestCallBack callBack) {
        SayAPI sayAPI = HttpMethods.getRetrofit().create(SayAPI.class);
        sayAPI.addComment(user.getStudentKH(),user.getRember_code(),id,commit)
                .compose(lifecycleTransformer)
                .map(new Func1<HttpResult,HttpResult>() {
                    @Override
                    public HttpResult call(HttpResult o) {
                        if(!"ok".equals(o.getMsg())){
                            if("令牌错误".equals(o.getMsg())) {
                                throw new UserLoginException();
                            }else{
                                throw new DataException(o.getMsg());
                            }
                        }
                        return o;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {

                    @Override
                    public void onCompleted() {
                        callBack.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestFail(e);
                    }

                    @Override
                    public void onNext(HttpResult says) {
                        callBack.requestSuccess(says);
                    }

                });
    }

    /**
     * 导入点赞数据实现 使用通用框架实现（带Dialog动画）
     * @param lifecycleTransformer
     * @param subscriber
     */
    @Override
    public void loadLikedSays(LifecycleTransformer lifecycleTransformer, final Subscriber<HttpResult<List<String>>> subscriber) {
        AddSayLikeAPI sayListAPI = HttpMethods.getRetrofit().create(AddSayLikeAPI.class);
        sayListAPI.getAllLikeSay(user.getStudentKH(), user.getRember_code())
                .compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void deleteSay(String id, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack) {
        // 暂时实现在Adapter中 方便与我的说说界面重用
    }

}
