package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.MyLostListAdapter;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的失物招领
 * Created by 高沛 on 16-11-10.
 */

public class MyLoseActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_mysaylist)
    LRecyclerView rvMylostlist;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Lose> loseList = new ArrayList<>();


    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_mysay;
    }

    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        tvToolbarTitle.setText("我的失物");
        getLoseList();
        rvMylostlist.setLayoutManager(new LinearLayoutManager(MyLoseActivity.this, LinearLayoutManager.VERTICAL, false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new MyLostListAdapter(MyLoseActivity.this, loseList));
        rvMylostlist.setAdapter(mLRecyclerViewAdapter);
        rvMylostlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                loseList.clear();
                getLoseList();
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
        rvMylostlist.setEmptyView(rlEmpty);
    }

    public void getLoseList() {
        User user = DBHelper.getUserDao().get(0);
        String user_id = user.getUser_id();

        SubscriberOnNextListener<HttpResult<PageData<Lose>>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<PageData<Lose>>>() {
            @Override
            public void onNext(HttpResult<PageData<Lose>> o) {
                if (o.getMsg().equals("ok")) {
                    loseList.addAll(o.getData().getPosts());
                } else if (o.getMsg().equals("令牌错误")) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                    finish();
                }
                rvMylostlist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getLoseListById(
                new ProgressSubscriber<HttpResult<PageData<Lose>>>(subscriberOnNextListener, MyLoseActivity.this)
                , user_id);
    }

    @OnClick({R.id.imgbtn_toolbar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
        }
    }
}
