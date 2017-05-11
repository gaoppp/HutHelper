package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改昵称
 * Created by 高沛 on 16-10-6.
 */

public class ChangeNickNameActivity extends BaseActivity {

    @BindView(R.id.et_chgnn_content)
    EditText etChgnnContent;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private String title;
    private User user;

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            title = parms.getString("title");
            user = (User) parms.getSerializable("user");
        } else {
            ToastUtil.showToastShort("数据传输失败");
            finish();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_changenickname;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText(title);
        etChgnnContent.setText(user.getUsername());
    }

    private void changeNickName(String name) {
        SubscriberOnNextListener changnn = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    ToastUtil.showToastShort("修改成功");
                    finish();
                } else if ("令牌错误".equals(o.getMsg())) {
                    startActivity(ImportActivity.class);
                    finish();
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                }
            }
        };
        HttpMethods.getInstance().ChangeUserName(
                new ProgressSubscriber<HttpResult>(changnn, ChangeNickNameActivity.this),
                user, name);
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.btn_chgnn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.btn_chgnn_ok:
                changeNickName(etChgnnContent.getText().toString());
                break;
        }
    }
}
