package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.CareerTalk;
import com.gaop.huthelper.presenter.event.CareerListPresenter;
import com.gaop.huthelper.presenter.impl.CareerListPresenterImpl;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.adapter.CareerListAdapter;
import com.gaop.huthelper.view.adapter.LoadMoreAdapterItemClickListener;
import com.gaop.huthelper.view.ui.CareerListView;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 高沛 on 2017/4/10.
 */

public class CareerTalkActivity extends BaseActivity implements CareerListView {


    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.rv_careertalk)
    RecyclerView rvCareerList;
    @BindView(R.id.RefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_toolbar_edit)
    TextView tvToolbarEdit;

    private int curPage = 0;
    private CareerListAdapter adapter;
    private CareerListPresenter careerListPresenter;
    private boolean isAll = true;

    private ArrayList<CareerTalk> talksList = new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_careertalk;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        careerListPresenter = new CareerListPresenterImpl(this);
        tvToolbarTitle.setText("宣讲会");
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                adapter.showLoading();
                if (isAll) {
                    careerListPresenter.requestAll(curPage, bindUntilEvent(ActivityEvent.STOP));
                } else {
                    careerListPresenter.requestHut(curPage, bindUntilEvent(ActivityEvent.STOP));
                }

            }
        });
        rvCareerList.setLayoutManager(new LinearLayoutManager(CareerTalkActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CareerListAdapter(CareerTalkActivity.this, talksList, bindUntilEvent(ActivityEvent.STOP));
        adapter.setOnItemClickListener(new LoadMoreAdapterItemClickListener() {
            @Override
            public void itemClickLinstener(int position) {
                Intent intent = new Intent(CareerTalkActivity.this, CareerTalkItemActivity.class);
                CareerTalk careerTalk = talksList.get(position);
                intent.putExtra("id", "" + careerTalk.getId());
                startActivity(intent);
            }

            @Override
            public void loadMore() {
                careerListPresenter.loadMore(isAll, ++curPage, bindUntilEvent(ActivityEvent.STOP));
            }

            @Override
            public void footViewClick() {
                careerListPresenter.loadMore(isAll, ++curPage, bindUntilEvent(ActivityEvent.STOP));
            }
        });
        rvCareerList.setAdapter(adapter);
        careerListPresenter.requestAll(1, bindUntilEvent(ActivityEvent.STOP));
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.tv_toolbar_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.tv_toolbar_edit:
                adapter.showLoading();
                curPage = 1;
                if (isAll) {
                    tvToolbarEdit.setText("省内");
                    careerListPresenter.requestHut(curPage, bindUntilEvent(ActivityEvent.STOP));
                    isAll = false;
                } else {
                    tvToolbarEdit.setText("校内");
                    isAll = true;
                    careerListPresenter.requestAll(curPage, bindUntilEvent(ActivityEvent.STOP));
                }
                break;
        }
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(ArrayList<CareerTalk> list) {
        talksList = list;
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataFail(Throwable e) {
        ToastUtil.showToastShort(e.getMessage());
        if (curPage == 1)
            refreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadMoreSuccess(ArrayList<CareerTalk> dataEntitiesList) {
        if (dataEntitiesList == null || dataEntitiesList.size() == 0) {
            adapter.showNoData();
            return;
        }
        talksList.addAll(dataEntitiesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreFail(Throwable e) {
        adapter.setCanLoading(true);
        adapter.setFootClickable(true);
        adapter.footShowClickText();
        curPage--;
        ToastUtil.showToastShort(e.getMessage());
    }

}
