package com.gaop.huthelper.model.impl;

import com.gaop.huthelper.model.entity.CareerTalk;
import com.gaop.huthelper.model.entity.CareerTalkData;
import com.gaop.huthelper.model.event.CareerListModel;
import com.gaop.huthelper.model.network.api.CareerTalkAPI;
import com.gaop.huthelper.model.network.exception.DataException;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.RequestCallBack;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 高沛 on 2017/4/21.
 */

public class CareerListModelImpl implements CareerListModel {
    @Override
    public void requestAll(String zone, int page, final LifecycleTransformer lifecycleTransformer, final RequestCallBack callBack) {
        CareerTalkAPI api = HttpMethods.getRetrofit().create(CareerTalkAPI.class);
        callBack.start(null);
        api.getCareerTalkList("after", zone, page)
                .compose(lifecycleTransformer)
                .map(new Func1<CareerTalkData<List<CareerTalk>>, List<CareerTalk>>() {
                    @Override
                    public List<CareerTalk> call(CareerTalkData<List<CareerTalk>> listCareerTalkData) {
                        if (!"success".equals(listCareerTalkData.getStatus())) {
                            throw new DataException(listCareerTalkData.getStatus());
                        }
                        return listCareerTalkData.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CareerTalk>>() {
                    @Override
                    public void onCompleted() {
                        callBack.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestFail(e);
                    }

                    @Override
                    public void onNext(List<CareerTalk> careerTalks) {
                        callBack.requestSuccess(careerTalks);
                    }

                });
    }

    @Override
    public void requestHut(int univ_id, String zone, int page, LifecycleTransformer lifecycleTransformer, final RequestCallBack callBack) {
        CareerTalkAPI api = HttpMethods.getRetrofit().create(CareerTalkAPI.class);
        callBack.start(null);
        api.getCareerTalkList(univ_id, "after", zone, page)
                .compose(lifecycleTransformer)
                .map(new Func1<CareerTalkData<List<CareerTalk>>, List<CareerTalk>>() {
                    @Override
                    public List<CareerTalk> call(CareerTalkData<List<CareerTalk>> listCareerTalkData) {
                        if (!"success".equals(listCareerTalkData.getStatus())) {
                            throw new DataException(listCareerTalkData.getStatus());
                        }
                        return listCareerTalkData.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CareerTalk>>() {
                    @Override
                    public void onCompleted() {
                        callBack.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestFail(e);
                    }

                    @Override
                    public void onNext(List<CareerTalk> careerTalks) {
                        callBack.requestSuccess(careerTalks);
                    }

                });
    }
}
