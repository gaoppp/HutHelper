package com.gaop.huthelper.view.ui;

import android.view.View;

import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.entity.HttpResult;

/**
 * Created by 高沛 on 2017/4/17.
 */

public interface SayView {

    void showLoading();

    void hideLoading();

    void showCommentWindow(final int positon,String id);

    void showMenuWindow(View parent);

    void loginAgin();

    void getSaysSuccess(HttpResult<PageData<Say>> sayList);

    void getSaysFail(Throwable e);

    void commitSuccess(int positon,String commit,HttpResult result);

    void commitFail(Throwable e);

    void loadMoreSuccess(HttpResult<PageData<Say>> sayList);

    void loadMoreFail(Throwable e);

}
