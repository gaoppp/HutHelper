package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.adapter.SayRVAdapter;
import com.gaop.huthelperdao.User;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-9-10.
 */
public class MySayListActivity extends BaseActivity {

    @BindView(R.id.rv_mysaylist)
    RecyclerView rvMysaylist;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    ArrayList<Say> Saylist = new ArrayList<>();

    String user_id;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

   // private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private SayRVAdapter adapter;

    @Override
    public void initParms(Bundle parms) {
        user_id = parms.getString("user_id");//user_id 为“my”是为当前用户的所有说说，否则为指定用户的id

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_mysay;
    }

    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        if ("my".equals(user_id))
            tvToolbarTitle.setText("我的说说");
        else
            tvToolbarTitle.setText(user_id + "的说说");

        getSayList();


        rvMysaylist.setLayoutManager(new LinearLayoutManager(MySayListActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter=new SayRVAdapter(MySayListActivity.this, Saylist,bindUntilEvent(ActivityEvent.STOP));
        //rvMysaylist.setAdapter(mLRecyclerViewAdapter);
        rvMysaylist.setAdapter(adapter);
//        rvMysaylist.setLScrollListener(new LRecyclerView.LScrollListener() {
//            @Override
//            public void onRefresh() {
//                Saylist = new ArrayList<>();
//                getSayList();
//            }
//
//            @Override
//            public void onScrollUp() {
//
//            }
//
//            @Override
//            public void onScrollDown() {
//
//            }
//
//            @Override
//            public void onBottom() {
//            }
//
//            @Override
//            public void onScrolled(int distanceX, int distanceY) {
//            }
//
//        });
//        rvMysaylist.setEmptyView(rlEmpty);
    }

    public void getSayList() {
        if ("my".equals(user_id)) {
            User user = DBHelper.getUserDao().get(0);
            user_id = user.getUser_id();
        }
        SubscriberOnNextListener<HttpResult<PageData<Say>>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<PageData<Say>>>() {
            @Override
            public void onNext(HttpResult<PageData<Say>> o) {
                if (o.getMsg().equals("ok")) {
                    Saylist.addAll(o.getData().getPosts());
                } else if (o.getMsg().equals("令牌错误")) {
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                    startActivity(ImportActivity.class);
                } else {
                    ToastUtil.showToastShort(o.getMsg());
                    finish();
                }
//                rvMysaylist.refreshComplete();
//                mLRecyclerViewAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getMySayList(
                new ProgressSubscriber<HttpResult<PageData<Say>>>(subscriberOnNextListener, MySayListActivity.this)
                , user_id);
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
