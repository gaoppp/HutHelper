package com.gaop.huthelper.net;

import android.content.Context;
import android.system.ErrnoException;
import android.util.Log;
import android.widget.Toast;

import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.utils.ToastUtil;

import rx.Subscriber;

/**
 * Created by gaop1 on 2016/7/24.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener subscriberOnNextListener;
    private ProgressDialogHandler progressDialogHandler;
    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        this.context = context;
        this.subscriberOnNextListener = subscriberOnNextListener;
        progressDialogHandler = new ProgressDialogHandler(context, this, true);
    }


    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();

       if(e instanceof java.net.ConnectException){
           //网络连接错误
            ToastUtil.showToastShort("网络连接失败！");
        }else if(e instanceof java.net.SocketTimeoutException){
            //连接超时
            ToastUtil.showToastShort("连接超时！");
        }else if(e instanceof retrofit2.adapter.rxjava.HttpException){
           ToastUtil.showToastShort("服务器错误");
        }
 //      else{
//           ToastUtil.showToastShort("数据异常");
//       }
    }

    @Override
    public void onNext(T t) {
        subscriberOnNextListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    private void showProgressDialog() {
        if (progressDialogHandler != null) {
            progressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialogHandler != null) {
            progressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            progressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }
}
