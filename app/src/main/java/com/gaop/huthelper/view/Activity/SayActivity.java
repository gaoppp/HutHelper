package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.Say;
import com.gaop.huthelper.Model.SayData;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.MarketRVAdapter;
import com.gaop.huthelper.adapter.SayRVAdapter;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-9-9.
 */
public class SayActivity extends BaseActivity {

    @BindView(R.id.iv_say_mysay)
    ImageView ivMysay;


    private int COUNT = 0;
    private int CURPage = 0;
    private boolean isRefresh;
    List<Say> Saylist = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_saylist)
    LRecyclerView rvSaylist;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_say;
    }

    @Override
    public void doBusiness(Context mContext) {


        ButterKnife.bind(this);
        toolbar.setTitle("说说");
        setSupportActionBar(toolbar);

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
        getSayList(1);


        rvSaylist.setLayoutManager(new LinearLayoutManager(SayActivity.this,LinearLayoutManager.VERTICAL,false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new SayRVAdapter(SayActivity.this, Saylist));
        rvSaylist.setAdapter(mLRecyclerViewAdapter);
        rvSaylist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                CURPage = 0;
                Saylist = new ArrayList<>();
                isRefresh = true;
                getSayList(1);
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
                Log.e("ds",CURPage+" "+COUNT);
                if (CURPage + 1 <= COUNT) {
                    ++CURPage;
                    getSayList(CURPage);
                }else
                    RecyclerViewStateUtils.setFooterViewState(rvSaylist, LoadingFooter.State.TheEnd);
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
               // Bundle mBundle = new Bundle();
               // mBundle.putString("id", Saylist.get(i).getId());
               //
                // startActivity(GoodsActivity.class, mBundle);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });

    }

    public void getSayList(int pagenum) {
        SubscriberOnNextListener<HttpResult<SayData>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<SayData>>() {
            @Override
            public void onNext(HttpResult<SayData> o) {
                if (o.getMsg().equals("ok")) {
                    COUNT = o.getData().getInfo().getPage_max();
                    CURPage =Integer.valueOf(o.getData().getInfo().getPage_cur());
                    Saylist.addAll(o.getData().getPosts());
                } else {
                    ToastUtil.showToastShort("获取服务器数据为空");
                }
                rvSaylist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getSayList(
                new ProgressSubscriber<HttpResult<SayData>>(subscriberOnNextListener, SayActivity.this)
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



    @OnClick(R.id.iv_say_mysay)
    public void onClick() {
        if(fastClick()){
            Bundle bundle=new Bundle();
            bundle.putString("user_id","my");
            startActivity(MySayListActivity.class,bundle);
        }


    }
}
