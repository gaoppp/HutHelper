package com.gaop.huthelper.presenter.impl;

import android.util.Log;

import com.gaop.huthelper.model.entity.CareerTalk;
import com.gaop.huthelper.model.event.CareerListModel;
import com.gaop.huthelper.model.impl.CareerListModelImpl;
import com.gaop.huthelper.net.RequestCallBack;
import com.gaop.huthelper.presenter.event.CareerListPresenter;
import com.gaop.huthelper.view.ui.CareerListView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高沛 on 2017/4/21.
 */

public class CareerListPresenterImpl implements CareerListPresenter {
    private static final String TAG = "CareerListPresenterImpl";
    private static final String ZONE = "cs";
    private static final int HUT_ID = 218;

    private CareerListModel careerListModel;
    private CareerListView careerListView;

    public CareerListPresenterImpl(CareerListView careerListView) {
        this.careerListModel = new CareerListModelImpl();
        this.careerListView = careerListView;
    }

    @Override
    public void requestAll(int page, LifecycleTransformer lifecycleTransformer) {
        careerListModel.requestAll(ZONE, page, lifecycleTransformer, new RequestCallBack<List<CareerTalk>>() {
            @Override
            public void start(List<CareerTalk> data) {
                careerListView.showProgress();
                Log.d(TAG, "start: ");
                //TODO 加载缓存数据
            }

            @Override
            public void requestSuccess(List<CareerTalk> data) {
                Log.d(TAG, "requestSuccess: ");
                careerListView.loadDataSuccess((ArrayList<CareerTalk>) data);
            }

            @Override
            public void requestFail(Throwable e) {
                careerListView.loadDataFail(e);
                careerListView.hideProgress();
            }

            @Override
            public void onCompleted() {
                careerListView.hideProgress();
            }
        });
    }

    @Override
    public void requestHut(int page, LifecycleTransformer lifecycleTransformer) {
        careerListModel.requestHut(HUT_ID, ZONE, page, lifecycleTransformer, new RequestCallBack<List<CareerTalk>>() {
            @Override
            public void start(List<CareerTalk> data) {
                careerListView.showProgress();
                //TODO 加载缓存数据
            }

            @Override
            public void requestSuccess(List<CareerTalk> data) {
                careerListView.loadDataSuccess((ArrayList<CareerTalk>) data);
            }

            @Override
            public void requestFail(Throwable e) {
                careerListView.loadDataFail(e);
                careerListView.hideProgress();
            }

            @Override
            public void onCompleted() {
                careerListView.hideProgress();
            }
        });
    }

    @Override
    public void loadMore(boolean loadAll, int page, LifecycleTransformer lifecycleTransformer) {
        if (loadAll) {
            careerListModel.requestAll(ZONE, page, lifecycleTransformer, new RequestCallBack<List<CareerTalk>>() {

                @Override
                public void start(List<CareerTalk> data) {
                    //TODO 加载缓存数据
                }

                @Override
                public void requestSuccess(List<CareerTalk> data) {
                    careerListView.loadMoreSuccess((ArrayList<CareerTalk>) data);
                }

                @Override
                public void requestFail(Throwable e) {
                    careerListView.loadMoreFail(e);
                    careerListView.hideProgress();
                }

                @Override
                public void onCompleted() {
                    careerListView.hideProgress();
                }
            });
        } else {
            careerListModel.requestHut( HUT_ID,ZONE, page, lifecycleTransformer, new RequestCallBack<List<CareerTalk>>() {

                @Override
                public void start(List<CareerTalk> data) {
                    //TODO 加载缓存数据
                }

                @Override
                public void requestSuccess(List<CareerTalk> data) {
                    careerListView.loadMoreSuccess((ArrayList<CareerTalk>) data);
                }

                @Override
                public void requestFail(Throwable e) {
                    careerListView.loadMoreFail(e);
                    careerListView.hideProgress();
                }

                @Override
                public void onCompleted() {
                    careerListView.hideProgress();
                }
            });
        }

    }
}
