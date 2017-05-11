package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.CareerTalkData;
import com.gaop.huthelper.model.entity.CareerTalkItem;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yzy on 2017/4/17.
 */

public class CareerTalkItemActivity extends BaseActivity {

    @BindView(R.id.careertalk_item_title)
    TextView titleview;
    @BindView(R.id.careertalk_item_from)
    TextView fromview;
    @BindView(R.id.careertalk_item_holdtime)
    TextView holdtimeview;
    @BindView(R.id.careertalk_item_address)
    TextView addressview;
    @BindView(R.id.careertalk_item_webview)
    WebView webView;
    @BindView(R.id.iv_careertalk_logo)
    ImageView ivCareertalkLogo;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    private String id;
    private CareerTalkItem careerTalkItem;

    @Override
    public void initParms(Bundle parms) {
        if (parms.containsKey("id")) {
            id = parms.getString("id");
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_careertalkitem;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("宣讲详情");
        getCareerTalkData(id);
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
        }
    }

    private void getCareerTalkData(String id) {
        SubscriberOnNextListener getData = new SubscriberOnNextListener<CareerTalkData<CareerTalkItem>>() {
            @Override
            public void onNext(CareerTalkData<CareerTalkItem> careerTalkItemCareerTalkData) {
                if ("success".equals(careerTalkItemCareerTalkData.getStatus())) {
                    careerTalkItem = careerTalkItemCareerTalkData.getData();
                    initView();
                } else {
                    ToastUtil.showToastShort(careerTalkItemCareerTalkData.getStatus());
                }
            }
        };
        HttpMethods.getInstance().getCareerTalkData(id, new ProgressSubscriber<CareerTalkData<CareerTalkItem>>(getData, CareerTalkItemActivity.this));
    }

    private void initView() {
        titleview.setText(careerTalkItem.getTitle());
        addressview.setText("地点:"+careerTalkItem.getAddress());
        fromview.setText("来源:"+careerTalkItem.getWeb());
        holdtimeview.setText("时间:"+careerTalkItem.getHoldtime());
        Picasso.with(CareerTalkItemActivity.this).load(careerTalkItem.getLogoUrl()).into(ivCareertalkLogo);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        webView.setVerticalScrollbarOverlay(false);
        webView.loadDataWithBaseURL(null, careerTalkItem.getContent(), "text/html", "utf-8", null);
    }

}
