package com.gaop.huthelper.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.Electric;
import com.gaop.huthelper.R;

/**
 * Created by 高沛 on 16-11-20.
 * 电费查询结果Dialog
 */

public class ElectricDialog {

    private Context mContext;
    private Dialog mDialog;
    private View mDialogContentView;

    public ElectricDialog(Context context, Electric data) {
        this.mContext = context;
        init(data);
    }

    private void init(Electric data) {
        mDialog = new Dialog(mContext, R.style.cusrom_dialog);
        mDialogContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_electric, null);
        //余额
        TextView balance = (TextView) mDialogContentView.findViewById(R.id.tv_eledialog_yue);
        //余电
        TextView eletric = (TextView) mDialogContentView.findViewById(R.id.tv_eledialog_yudian);
        Button ok = (Button) mDialogContentView.findViewById(R.id.btn_eledialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        balance.setText(data.getPrize() + "元");
        eletric.setText(data.getOddl() + "度");
        mDialog.setContentView(mDialogContentView);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void setCanledOnTouchOutside(boolean canle) {
        mDialog.setCanceledOnTouchOutside(canle);
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }
}
