package com.gaop.huthelper.model.event;

import com.gaop.huthelper.net.RequestCallBack;
import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by 高沛 on 2017/4/21.
 */

public interface CareerListModel {

    void requestAll(String zone, int page, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);

    void requestHut(int univ_id, String zone, int page, LifecycleTransformer lifecycleTransformer, RequestCallBack callBack);
}
