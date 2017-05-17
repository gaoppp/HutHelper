package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelperdao.Notice;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeItemActivity extends BaseActivity {


    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private TextView mTitle, mContent, mTime;
    private Notice mNotice;

    @Override
    public void initParms(Bundle parms) {
        mNotice = (Notice) parms.getSerializable("notice");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_notice_item;
    }

    @Override
    public void doBusiness(Context mContext) {
        init();
        initDate();
    }

    private void init() {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("通知详情");
        mContent = (TextView) findViewById(R.id.tv_noticeitem_content);
        mTime = (TextView) findViewById(R.id.tv_noticeitem_time);
        mTitle = (TextView) findViewById(R.id.tv_noticeitem_title);
    }

    private void initDate() {
        mTitle.setText(mNotice.getTitle());
        mTime.setText(mNotice.getTime());
        mContent.setText(mNotice.getContent());
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
