package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/5/15.
 */
public class ImportActivity extends BaseActivity {

    @BindView(R.id.et_import_num)
    EditText etImportNum;
    @BindView(R.id.et_import_password)
    EditText etImportPassword;
    @BindView(R.id.btn_ok_import)
    Button btnOkImport;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_import;
    }


    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
    }


    private boolean isNumValid(String num) {
        return num.length() == 11;
    }

    public void ImportData() {
        SubscriberOnNextListener getUserData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o))
                    enterMain();
                else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getUserData(ImportActivity.this,
                new ProgressSubscriber<String>(getUserData, ImportActivity.this),
                etImportNum.getText().toString(), etImportPassword.getText().toString());
    }

    private void enterMain() {
        startActivity(new Intent(ImportActivity.this, MainActivity.class));
        finish();
    }


    @OnClick(R.id.btn_ok_import)
    public void onClick() {
        if (fastClick()) {
            if (isNumValid(etImportNum.getText().toString())) {
                ImportData();
            } else
                ToastUtil.showToastShort("学号有误");
        }
    }
}
