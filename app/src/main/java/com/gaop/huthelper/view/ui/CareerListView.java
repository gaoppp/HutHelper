package com.gaop.huthelper.view.ui;

import com.gaop.huthelper.model.entity.CareerTalk;

import java.util.ArrayList;

/**
 * Created by 高沛 on 2017/4/21.
 */

public interface CareerListView {
    void showProgress();

    void hideProgress();

    void loadDataSuccess(ArrayList<CareerTalk> list);

    void loadDataFail(Throwable e);

    void loadMoreSuccess(ArrayList<CareerTalk> dataEntitiesList);

    void loadMoreFail(Throwable e);
}
