package com.gaop.huthelper.net;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.gaop.huthelper.PinWheel.LoadingDiolog;

/**
 * 处理网络访问Dialog Handler
 * Created by gaop1 on 2016/7/24.
 */
public class ProgressDialogHandler extends Handler {


    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private LoadingDiolog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener progressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener progressCancelListener, boolean cancelable) {

        super();
        this.context = context;
        this.progressCancelListener = progressCancelListener;
        this.cancelable = cancelable;

    }

    private void initProgressDialog() {
        if (pd == null) {
            pd = new LoadingDiolog(context);
            pd.setCancelable(cancelable);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
