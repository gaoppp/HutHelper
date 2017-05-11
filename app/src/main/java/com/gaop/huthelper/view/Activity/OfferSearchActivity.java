package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaop.huthelper.model.entity.OfferData;
import com.gaop.huthelper.model.entity.OfferInfo;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.OfferListAdapter;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-11-15.
 */

public class OfferSearchActivity extends BaseActivity {

    @BindView(R.id.et_offerseacher_content)
    EditText etOfferseacherContent;
    @BindView(R.id.rl_offerseacher_list)
    LRecyclerView rlOfferseacherList;
    @BindView(R.id.tv_offersearch_empty)
    TextView tvOffersearchEmpty;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<OfferInfo> offerList = new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_searchoffer;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        rlOfferseacherList.setLayoutManager(new LinearLayoutManager(OfferSearchActivity.this, LinearLayoutManager.VERTICAL, false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new OfferListAdapter(OfferSearchActivity.this, offerList));
        rlOfferseacherList.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("offerinfo", offerList.get(i));
                startActivity(OfferItemActivity.class, bundle);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
        rlOfferseacherList.setAdapter(mLRecyclerViewAdapter);
        etOfferseacherContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    searchOffer();
                    return true;
                }

                return false;
            }
        });
        CommUtil.showSoftInput(OfferSearchActivity.this, etOfferseacherContent);

    }

    private void searchOffer() {
        String content = etOfferseacherContent.getText().toString();
        if ("".equals(content)) {
            ToastUtil.showToastShort("搜索内容不能为空");
            return;
        }
        String token = PrefUtil.getString(OfferSearchActivity.this, "OfferToken", "");
        if (token == "") {
            getToken();
        } else {
            SubscriberOnNextListener<OfferData> subscriberOnNextListener = new SubscriberOnNextListener<OfferData>() {
                @Override
                public void onNext(OfferData data) {
                    if (data.getR() == 1) {
                        offerList.clear();
                        if (data.getInfo().size() == 0) {
                            tvOffersearchEmpty.setVisibility(View.VISIBLE);
                        } else {
                            tvOffersearchEmpty.setVisibility(View.GONE);
                            offerList.addAll(data.getInfo());
                        }
                    } else {
                        if ("你的access_token不存在或是已经过期，此参数如若需要请与开发者联系".equals(data.getMsg())) {
                            getToken();
                        } else {
                            ToastUtil.showToastShort(data.getMsg());
                        }
                    }
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
            };
            HttpMethods.getInstance().searchOffer(
                    new ProgressSubscriber<OfferData>(subscriberOnNextListener, OfferSearchActivity.this)
                    , token, content);
        }

    }

    private void getToken() {
        SubscriberOnNextListener<String> subscriberOnNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                if ("ok".equals(data)) {
                    searchOffer();
                } else {
                    ToastUtil.showToastShort(data);
                }
            }
        };
        HttpMethods.getInstance().getOfferToken(OfferSearchActivity.this,
                new ProgressSubscriber<String>(subscriberOnNextListener, OfferSearchActivity.this));
    }


    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
