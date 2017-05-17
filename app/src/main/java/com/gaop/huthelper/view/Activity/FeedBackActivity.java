package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ButtonUtils;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 反馈Activity
 * Created by 高沛 on 2016/5/26.
 */
public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.et_feedbk_content)
    EditText etFeedbkContent;
    @BindView(R.id.et_feedbk_tel)
    EditText etFeedbkTel;
    @BindView(R.id.ll_feedbk)
    LinearLayout llFeedbk;
    @BindView(R.id.ll_feebk_success)
    LinearLayout llFeebkSuccess;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("反馈");

    }

    /**
     * 反馈
     */
    private void feedBack() {
        String content = etFeedbkContent.getText().toString();
        String tel = etFeedbkTel.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToastShort("反馈意见不能为空");
            return;
        } else {
            String version = null, model = null;
            try {
                PackageManager pm = this.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                version = "版本号 ：" + pi.versionName + " (" + pi.versionCode + ")";
                model = "机型：" + Build.MANUFACTURER + Build.MODEL + " (Android" + Build.VERSION.RELEASE + ")";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            User user = DBHelper.getUserDao().get(0);
            content = "来源：" + user.getStudentKH() + " Android \n内容：" + content;
            if (!TextUtils.isEmpty(version) && !TextUtils.isEmpty(model)) {
                content = content + "\n" + version + "\n" + model;
            }
            SubscriberOnNextListener getData = new SubscriberOnNextListener<String>() {
                @Override
                public void onNext(String o) {
                    llFeedbk.setVisibility(View.GONE);
                    llFeebkSuccess.setVisibility(View.VISIBLE);
                }
            };
            HttpMethods.getInstance().feedBack(
                    new ProgressSubscriber<String>(getData, FeedBackActivity.this),
                    tel, content);
        }
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.btn_feedbk_ok, R.id.btn_feedbk_finish, R.id.btn_feedbk_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.btn_feedbk_ok:
                if (!ButtonUtils.isFastDoubleClick(-1, 2000)) {
                    CommUtil.hideSoftInput(FeedBackActivity.this);
                    feedBack();
                }
                break;
            case R.id.btn_feedbk_finish:
                finish();
                break;
            case R.id.btn_feedbk_again:
                llFeebkSuccess.setVisibility(View.GONE);
                llFeedbk.setVisibility(View.VISIBLE);
                etFeedbkContent.setText("");
                break;
        }
    }
}
