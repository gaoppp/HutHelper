package com.gaop.huthelper.PinWheel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.gaop.huthelper.R;


/**
 * 自定义Diolog
 * Created by gaop1 on 2016/4/22.
 */
public class PinWheelDiolog {
    private Context mContext;
    private Dialog mDialog;
    private PinWheelView mPinWheelView;
    private View mDialogContentView;
    public PinWheelDiolog(Context context){
        this.mContext=context;
        init();
    }
    private void init(){
        mDialog=new Dialog(mContext, R.style.cusrom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.pinwheel_dialog,null);
        mPinWheelView= (PinWheelView) mDialogContentView.findViewById(R.id.pinwheelview);
        mDialog.setContentView(mDialogContentView);
    }

    public void show(){
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
