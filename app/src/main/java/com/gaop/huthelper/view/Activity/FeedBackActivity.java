package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gaop.huthelper.Model.Electric;
import com.gaop.huthelper.R;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;


/**
 * Created by gaop1 on 2016/5/26.
 */
public class FeedBackActivity extends BaseActivity {
    private TextView mTvTel, mTvContent;
    private Button mFeedBk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolba_feedback);
        mToolbar.setTitle("反馈意见");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvContent = (TextView) findViewById(R.id.et_feedbk_content);
        mTvTel = (TextView) findViewById(R.id.et_feedbk_tel);
        mFeedBk = (Button) findViewById(R.id.btn_feedbk_ok);
        mFeedBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fastClick()) {
                    CommUtil.hideSoftInput(FeedBackActivity.this);
                    feedBack();
                }
            }
        });

    }

    private void feedBack() {
        String content = mTvContent.getText().toString();
        String tel = mTvTel.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mTvContent.setError("反馈内容不能为空！");
            mTvContent.setSelected(true);
            return;
        }else{
            SubscriberOnNextListener getElectricData = new SubscriberOnNextListener<String>() {
                @Override
                public void onNext(String o) {
                    ToastUtil.showToastShort("反馈成功！");
                }
            };
            HttpMethods.getInstance().feedBack(
                    new ProgressSubscriber<String>(getElectricData, FeedBackActivity.this),
                    tel,content);
        }


    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

}
