package com.gaop.huthelper.presenter.impl;

import android.content.Context;

import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.entity.SayLikeCache;
import com.gaop.huthelper.model.event.SayModel;
import com.gaop.huthelper.model.impl.SayModelImpl;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.network.exception.UserLoginException;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.net.RequestCallBack;
import com.gaop.huthelper.presenter.event.SayPresenter;
import com.gaop.huthelper.view.ui.SayView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import static com.gaop.huthelper.utils.ToastUtil.showToastShort;

/**
 * 校园说说Presenter实现类
 * Created by 高沛 on 2017/4/18.
 */

public class SayPresenterImpl implements SayPresenter {

    private SayModel sayModel;
    private SayView sayView;

    public SayPresenterImpl(SayView sayView) {
        this.sayModel = new SayModelImpl();
        this.sayView = sayView;
    }



    /**
     * 请求说说数据
     *
     * @param page                 页码
     * @param lifecycleTransformer 生命周期管理
     */
    @Override
    public void request(int page, LifecycleTransformer lifecycleTransformer) {
        sayModel.request(page, lifecycleTransformer, new RequestCallBack<HttpResult<PageData<Say>>>() {
            @Override
            public void start(HttpResult<PageData<Say>> sayList) {
                //TODO 加载缓存数据
                sayView.showLoading();
            }

            @Override
            public void requestSuccess(HttpResult<PageData<Say>> sayList) {
                sayView.getSaysSuccess(sayList);
            }

            @Override
            public void requestFail(Throwable e) {
                sayView.getSaysFail(e);
                sayView.hideLoading();
            }

            @Override
            public void onCompleted() {
                sayView.hideLoading();
            }
        });
    }

    /**
     * 获取点赞数据（居然要先获取点赞数据才能去获取说说，api真坑。。。）
     *
     * @param context              上下文
     * @param lifecycleTransformer
     */
    @Override
    public void loadLikedSays(Context context, final LifecycleTransformer lifecycleTransformer) {

        SubscriberOnNextListener<HttpResult<List<String>>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<List<String>>>() {
            @Override
            public void onNext(HttpResult<List<String>> data) {
                if ("成功获得点赞数据".equals(data.getMsg())) {
                    SayLikeCache.setLikeList(data.getData());
                    sayView.showLoading();
                    request(1, lifecycleTransformer);
                } else if (data.getMsg().equals("令牌错误")) {
                    sayView.loginAgin();
                } else {
                    showToastShort(data.getMsg());
                }
            }
        };
        sayModel.loadLikedSays(lifecycleTransformer, new ProgressSubscriber<HttpResult<List<String>>>(subscriberOnNextListener, context));
    }

    /**
     * 评论说说
     *
     * @param positon              位置 用于成功发表后添加数据
     * @param id                   说说id
     * @param commit               内容
     * @param lifecycleTransformer
     */
    @Override
    public void addCommit(final int positon, String id, final String commit, LifecycleTransformer lifecycleTransformer) {
        sayModel.commitSay(id, commit, lifecycleTransformer, new RequestCallBack<HttpResult>() {
            @Override
            public void start(HttpResult result) {

            }

            @Override
            public void requestSuccess(HttpResult result) {
                sayView.commitSuccess(positon, commit, result);
            }

            @Override
            public void requestFail(Throwable e) {
                if (e instanceof UserLoginException) {
                    sayView.loginAgin();
                    return;
                }
                sayView.getSaysFail(e);
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    @Override
    public void loadMore(int page, LifecycleTransformer lifecycleTransformer) {
        sayModel.request(page, lifecycleTransformer, new RequestCallBack<HttpResult<PageData<Say>>>() {
            @Override
            public void start(HttpResult<PageData<Say>> sayList) {
                //TODO 加载缓存数据
            }

            @Override
            public void requestSuccess(HttpResult<PageData<Say>> sayList) {
                sayView.loadMoreSuccess(sayList);
            }

            @Override
            public void requestFail(Throwable e) {
                sayView.loadMoreFail(e);
                sayView.hideLoading();
            }

            @Override
            public void onCompleted() {
                sayView.hideLoading();
            }
        });
    }

}
