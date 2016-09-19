package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.Say;
import com.gaop.huthelper.Model.SayData;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.SayRVAdapter;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaop on 16-9-10.
 */
public class MySayListActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_mysaylist)
    LRecyclerView rvMysaylist;
    @BindView(R.id.tv_say_empty)
    TextView tv_empty;

    List<Say> Saylist = new ArrayList<>();

    String user_id;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void initParms(Bundle parms) {
        user_id=parms.getString("user_id");//user_id 为“my”是为当前用户的所有说说，否则为指定用户的id

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_mysay;
    }

    @Override
    public void doBusiness(Context mContext) {

        ButterKnife.bind(this);
        if("my".equals(user_id))
        toolbar.setTitle("我的说说");
        else
        toolbar.setTitle(user_id+"的说说");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSayList();


        rvMysaylist.setLayoutManager(new LinearLayoutManager(MySayListActivity.this,LinearLayoutManager.VERTICAL,false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new SayRVAdapter(MySayListActivity.this, Saylist));
        rvMysaylist.setAdapter(mLRecyclerViewAdapter);
        rvMysaylist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                Saylist = new ArrayList<>();
                getSayList();
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
        rvMysaylist.setEmptyView(tv_empty);

    }
    public void getSayList() {
        if("my".equals(user_id)){
            User user= DBHelper.getUserDao().get(0);
            user_id=user.getUser_id();
        }

        SubscriberOnNextListener<HttpResult<SayData>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<SayData>>() {
            @Override
            public void onNext(HttpResult<SayData> o) {
                if (o.getMsg().equals("ok")) {
                    Saylist.addAll(o.getData().getPosts());
                } else if (o.getMsg().equals("令牌错误")) {
                ToastUtil.showToastShort("账号异地登录，请重新登录");
                startActivity(ImportActivity.class);
               } else {
                ToastUtil.showToastShort(o.getMsg());
                finish();
              }
                rvMysaylist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getMySayList(
                new ProgressSubscriber<HttpResult<SayData>>(subscriberOnNextListener, MySayListActivity.this)
                , user_id);
    }


}
