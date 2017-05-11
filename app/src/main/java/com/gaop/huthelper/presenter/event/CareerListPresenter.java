package com.gaop.huthelper.presenter.event;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by 高沛 on 2017/4/21.
 */

public interface CareerListPresenter {
    void requestAll(int page, LifecycleTransformer lifecycleTransformer);

    void requestHut(int page, LifecycleTransformer lifecycleTransformer);

    void loadMore(boolean loadAll,int page, LifecycleTransformer lifecycleTransformer);
}
