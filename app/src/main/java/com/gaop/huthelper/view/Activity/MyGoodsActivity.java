package com.gaop.huthelper.view.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.MyGoodsItem;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.MyGoodsAdapter;
import com.gaop.huthelper.jiekou.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2016/9/4.
 */
public class MyGoodsActivity extends BaseActivity {


    @BindView(R.id.rv_marketlist)
    LRecyclerView rvMarketlist;
    @BindView(R.id.tv_goods_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<MyGoodsItem> Goodslist;

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
        Goodslist = new ArrayList<>();
        rvMarketlist.setLayoutManager(new LinearLayoutManager(MyGoodsActivity.this, LinearLayoutManager.VERTICAL, false));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new MyGoodsAdapter(MyGoodsActivity.this, Goodslist));
        getGoodsList();
        rvMarketlist.setEmptyView(tvEmpty);
        rvMarketlist.setAdapter(mLRecyclerViewAdapter);
        rvMarketlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
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
                startActivity(GoodsActivity.class, mBundle);
            }

            @Override
            public void onItemLongClick(View view, int i) {
                dialog(i);
            }
        });
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyGoodsActivity.this);
        builder.setMessage("确认移除该商品吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteGoods(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void getGoodsList() {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener<HttpResult<List<MyGoodsItem>>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<List<MyGoodsItem>>>() {
            @Override
            public void onNext(HttpResult<List<MyGoodsItem>> o) {
                if (o.getMsg().equals("ok")) {
                    if (o.getData() != null) {
                        Goodslist.clear();
                        for (MyGoodsItem i : o.getData()) {
                            Goodslist.add(i);
                        }
                        rvMarketlist.refreshComplete();
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }//else
                    //rvMarketlist.setEmptyView();

                } else if ("令牌错误".equals(o.getMsg())) {
                    startActivity(ImportActivity.class);
                    ToastUtil.showToastShort("账号异地登录，请重新登录");

                } else
                    ToastUtil.showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().getMyGoodsList(
                new ProgressSubscriber<HttpResult<List<MyGoodsItem>>>(subscriberOnNextListener, MyGoodsActivity.this)
                , user.getStudentKH(), user.getRember_code());
    }


    private void deleteGoods(final int position) {
        User user = DBHelper.getUserDao().get(0);
        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                if (o.getMsg().equals("ok")) {
                    Goodslist.remove(position);
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                } else if ("令牌错误".equals(o.getMsg())) {
                    startActivity(ImportActivity.class);
                    ToastUtil.showToastShort("账号异地登录，请重新登录");
                } else
                    ToastUtil.showToastShort(o.getMsg());
            }
        };
        HttpMethods.getInstance().deleteGoods(
                new ProgressSubscriber<HttpResult>(subscriberOnNextListener, MyGoodsActivity.this)
                , user.getStudentKH(), user.getRember_code(), Goodslist.get(position).getId());
    }


    @OnClick(R.id.imgbtn_toolbar_back)
    public void onClick() {
        finish();
    }
}
