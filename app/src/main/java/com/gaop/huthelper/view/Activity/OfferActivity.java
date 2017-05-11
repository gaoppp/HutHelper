package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.OfferListAdapter;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.OfferCache;
import com.gaop.huthelper.model.entity.OfferData;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 高沛 on 16-11-14.
 */

public class OfferActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rl_offerlist)
    LRecyclerView rlOfferlist;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int type = OfferCache.ID_SORT_SJ;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_offer;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("校招薪水");
        OfferCache.init(OfferActivity.this);
        rlOfferlist.setLayoutManager(new LinearLayoutManager(OfferActivity.this, LinearLayoutManager.VERTICAL, false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new OfferListAdapter(OfferActivity.this, OfferCache.getSort_items()));
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("offerinfo", OfferCache.getSort_items().get(i));
                startActivity(OfferItemActivity.class, bundle);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
        rlOfferlist.setAdapter(mLRecyclerViewAdapter);
        rlOfferlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                getOffer();
            }

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });
        getOffer();
    }

    private void getOffer() {
        String token = PrefUtil.getString(OfferActivity.this, "OfferToken", "");
        if (token == "") {
            getToken();
        } else {
            if (OfferCache.getSize() != 0) {
                if (type != OfferCache.ID_SORT_SJ) {
                    OfferCache.sortByShijian();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
                rlOfferlist.refreshComplete();

            } else {
                OfferCache.clear();
                SubscriberOnNextListener<OfferData> subscriberOnNextListener = new SubscriberOnNextListener<OfferData>() {
                    @Override
                    public void onNext(OfferData data) {
                        if (data.getR() == 1) {
                            OfferCache.addAll(data.getInfo());
                        } else {
                            if ("你的access_token不存在或是已经过期，此参数如若需要请与开发者联系".equals(data.getMsg())) {
                                getToken();
                            } else {
                                ToastUtil.showToastShort(data.getMsg());
                            }
                        }
                        rlOfferlist.refreshComplete();
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                };
                HttpMethods.getInstance().getOfferList(
                        new ProgressSubscriber<OfferData>(subscriberOnNextListener, OfferActivity.this)
                        , token);
            }
        }
    }

    private void getToken() {
        SubscriberOnNextListener<String> subscriberOnNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                if ("ok".equals(data)) {
                    getOffer();
                } else {
                    ToastUtil.showToastShort(data);
                }
            }
        };
        HttpMethods.getInstance().getOfferToken(OfferActivity.this,
                new ProgressSubscriber<String>(subscriberOnNextListener, OfferActivity.this));
    }

    private void initCompChoose() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_ompchoose, null);//获取自定义布局
        builder.setView(layout);
        builder.setIcon(null);//设置标题图标
        builder.setTitle(null);//设置标题内容
        final AlertDialog dlg = builder.create();
        Button number = (Button) layout.findViewById(R.id.btn_offer_sortbynumber);
        Button score = (Button) layout.findViewById(R.id.btn_offer_sortbyscore);
        Button time = (Button) layout.findViewById(R.id.btn_offer_sortbytime);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != OfferCache.ID_SORT_LL) {
                    OfferCache.sortByLiulan();
                    type = OfferCache.ID_SORT_LL;
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
                dlg.dismiss();
            }
        });
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != OfferCache.ID_SORT_KXD) {
                    OfferCache.sortByKexindu();
                    type = OfferCache.ID_SORT_KXD;
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
                dlg.dismiss();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != OfferCache.ID_SORT_SJ) {
                    OfferCache.sortByShijian();
                    type = OfferCache.ID_SORT_SJ;
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
                dlg.dismiss();
            }
        });
        //final AlertDialog dlg = builder.create();
        dlg.show();
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.iv_offer_search, R.id.iv_offer_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.iv_offer_search:
                startActivity(OfferSearchActivity.class);
                break;
            case R.id.iv_offer_sort:
                initCompChoose();
                break;
        }
    }
}

