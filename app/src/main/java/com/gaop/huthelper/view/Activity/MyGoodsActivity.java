package com.gaop.huthelper.view.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.GoodsListItem;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.MarketRVAdapter;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * 我的商品
 * Created by 高沛 on 2016/9/4.
 */
public class MyGoodsActivity extends BaseActivity {

    @BindView(R.id.rv_marketlist)
    LRecyclerView rvMarketlist;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<GoodsListItem> Goodslist=new ArrayList<>();

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_mygoods;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("我的商品");
        rvMarketlist.setEmptyView(rlEmpty);
        rvMarketlist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new MarketRVAdapter(MyGoodsActivity.this, Goodslist));
        rvMarketlist.setAdapter(mLRecyclerViewAdapter);
        rvMarketlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                Goodslist.clear();
                getGoodsList();
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
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle mBundle = new Bundle();
                mBundle.putString("id", Goodslist.get(i).getId());
                mBundle.putBoolean("delete",true);

                if (android.os.Build.VERSION.SDK_INT > 20) {
                    Intent intent = new Intent(MyGoodsActivity.this, GoodsActivity.class);
                    intent.putExtras(mBundle);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MyGoodsActivity.this, view, "goodstransition").toBundle());
                } else {
                    startActivity(GoodsActivity.class, mBundle);
                }

            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
        getGoodsList();
    }

    public void getGoodsList() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener<ResponseBody> subscriberOnNextListener = new SubscriberOnNextListener<ResponseBody>() {
            @Override
            public void onNext(ResponseBody o) {
                try {
                    String s=o.string();
                    Gson gson=new Gson();
                    JsonParser parser = new JsonParser();
                    JsonElement el = parser.parse(s);
                    JsonArray jsonArray = null;
                    if(el.isJsonArray()){
                        jsonArray = el.getAsJsonArray();
                    }else{
                        if(el.isJsonObject()) {
                            HttpResult h = gson.fromJson(el, HttpResult.class);
                            if(h.getMsg().equals("令牌错误")){
                                startActivity(ImportActivity.class);
                                ToastUtil.showToastShort("账号异地登录，请重新登录");
                                finish();
                            }
                        }else {
                            ToastUtil.showToastShort("数据异常");
                            return;
                        }

                    }
                    Goodslist.clear();
                    GoodsListItem field = null;
                    Iterator it = jsonArray.iterator();
                    while(it.hasNext()){
                        JsonElement e = (JsonElement)it.next();
                        field = gson.fromJson(e, GoodsListItem.class);
                        Goodslist.add(field);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showToastShort("获取数据异常");
                    return;
                }
                rvMarketlist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        HttpMethods.getInstance().getMyGoodsList(
                new ProgressSubscriber<ResponseBody>(subscriberOnNextListener, MyGoodsActivity.this)
                , user.getStudentKH(), user.getRember_code());
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
