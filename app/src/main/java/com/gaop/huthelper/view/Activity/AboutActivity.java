package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gaop.huthelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于Activity
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.imgbtn_toolbar_back)
    ImageButton imgbtnToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("关于");
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
