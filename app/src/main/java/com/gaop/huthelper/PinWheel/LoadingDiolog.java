package com.gaop.huthelper.PinWheel;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.gaop.huthelper.R;


/**
 * 自定义Diolog
 * Created by 高沛 on 2016/4/22.
 */
public class LoadingDiolog {
    private Context mContext;
    private Dialog mDialog;
    private ImageView loadingView;
    private View mDialogContentView;
    public LoadingDiolog(Context context){
        this.mContext=context;
        init();
    }
    private void init(){
        mDialog=new Dialog(mContext, R.style.cusrom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.loading_dialog,null);
         loadingView= (ImageView) mDialogContentView.findViewById(R.id.iv_loading);
        mDialog.setContentView(mDialogContentView);
    }

    public void show(){
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(loadingView,"rotation",0,360);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(800);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.start();

        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public Dialog getDialog(){
        return mDialog;
    }

    public void setCanledOnTouchOutside(boolean canle){
        mDialog.setCanceledOnTouchOutside(canle);
    }
    public void setCancelable(boolean cancelable){
        mDialog.setCancelable(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener){
        mDialog.setOnCancelListener(listener);
    }
    public boolean isShowing(){
        return mDialog.isShowing();
    }
}
