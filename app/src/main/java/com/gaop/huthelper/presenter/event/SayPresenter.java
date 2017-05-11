package com.gaop.huthelper.presenter.event;

import android.content.Context;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by gaop1 on 2017/4/18.
 */

public interface SayPresenter {
    void request(int page, LifecycleTransformer lifecycleTransformer);

    void loadLikedSays(Context context, LifecycleTransformer lifecycleTransformer);

    void addCommit(int positon,String id, String commit, LifecycleTransformer lifecycleTransformer);

    void loadMore(int page, LifecycleTransformer lifecycleTransformer);
}
