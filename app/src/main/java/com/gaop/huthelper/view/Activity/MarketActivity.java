package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.MarketRVAdapter;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketActivity extends BaseActivity {

    @BindView(R.id.iv_market_mygoods)
    ImageView ivMarketMygoods;
    private int COUNT = 0;
    private int CURPage = 0;
    private boolean isRefresh;
    List<GoodsListItem> Goodslist = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_marketlist)
    LRecyclerView rvMarketlist;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_market;
    }


    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("二手市场");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddGoodsActivity.class);
            }
        });
        getGoodsList(1);
        rvMarketlist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new MarketRVAdapter(MarketActivity.this, Goodslist));
        rvMarketlist.setAdapter(mLRecyclerViewAdapter);
        rvMarketlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                CURPage = 0;
                Goodslist = new ArrayList<>();
                isRefresh = true;
                getGoodsList(1);
            }

            @Override
            public void onScrollUp() {
                hideViews();
            }

            @Override
            public void onScrollDown() {
                showViews();
            }

            @Override
            public void onBottom() {
                if (CURPage + 1 <= COUNT) {
                    ++CURPage;
                    getGoodsList(CURPage);
                }else
                RecyclerViewStateUtils.setFooterViewState(rvMarketlist, LoadingFooter.State.TheEnd);
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle mBundle = new Bundle();
                mBundle.putString("id", Goodslist.get(i).getId());
                startActivity(GoodsActivity.class, mBundle);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }


    public void getGoodsList(int pagenum) {
        SubscriberOnNextListener<GoodsListItem[]> subscriberOnNextListener = new SubscriberOnNextListener<GoodsListItem[]>() {
            @Override
            public void onNext(GoodsListItem[] jsonArray) {
                if (jsonArray != null) {
                    COUNT = jsonArray[0].getPage_max();
                    CURPage = Integer.parseInt(jsonArray[0].getPage_cur());
                    int count = jsonArray.length;
                    for (int i = 1; i < count; i++) {
                        Goodslist.add(jsonArray[i]);
                    }
                } else {
                    ToastUtil.showToastShort("获取服务器数据为空");
                }
                rvMarketlist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getGoodsList(
                new ProgressSubscriber<GoodsListItem[]>(subscriberOnNextListener, MarketActivity.this)
                , pagenum);
    }

    private void hideViews() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        fab.animate().translationY(fab.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }



    @OnClick(R.id.iv_market_mygoods)
    public void onClick() {
        if(fastClick())
            startActivity(MyGoodsActivity.class);
    }

    public abstract class RecycleViewScroller extends RecyclerView.OnScrollListener implements LRecyclerView.LScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onRefresh() {
            CURPage = 0;
            Goodslist = new ArrayList<>();
            RecyclerViewStateUtils.setFooterViewState(rvMarketlist, LoadingFooter.State.Normal);
            isRefresh = true;
            getGoodsList(0);
        }

        @Override
        public void onScrollUp() {
        }

        @Override
        public void onScrollDown() {
        }

        @Override
        public void onBottom() {
            Log.e("tag", CURPage + "  " + COUNT);
            if (CURPage + 1 <= COUNT) {
                ++CURPage;
                Log.e("ee", "here");
                getGoodsList(CURPage);
            }
            // do something, such as load next page.
        }

        @Override
        public void onScrolled(int distanceX, int distanceY) {
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            final Picasso picasso = Picasso.with(MarketActivity.this);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                picasso.resumeTag(MarketActivity.this);
            } else {
                picasso.pauseTag(MarketActivity.this);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        public abstract void onHide();

        public abstract void onShow();
    }


}
