package com.gaop.huthelper.view.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.entity.GoodsListItem;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ButtonUtils;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.adapter.MarketRVAdapter;
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

public class MarketActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.imgbtn_toolbar_menu)
    ImageButton imageButton;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private int COUNT = 0;
    private int CURPage = 0;
    private boolean isRefresh;
    List<GoodsListItem> Goodslist = new ArrayList<>();

    @BindView(R.id.rv_marketlist)
    LRecyclerView rvMarketlist;

    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_market;
    }


    @Override
    public void doBusiness(final Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("二手市场");
        rvMarketlist.setEmptyView(rlEmpty);

        rvMarketlist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new MarketRVAdapter(MarketActivity.this, Goodslist));
        rvMarketlist.setAdapter(mLRecyclerViewAdapter);
        rvMarketlist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                CURPage = 0;
                Goodslist.clear();
                isRefresh = true;
                getGoodsList(1);
            }

            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                if (CURPage + 1 <= COUNT) {
                    ++CURPage;
                    getGoodsList(CURPage);
                } else
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

                if (android.os.Build.VERSION.SDK_INT > 20) {
                    Intent intent = new Intent(MarketActivity.this, GoodsActivity.class);
                    intent.putExtras(mBundle);
                    MarketActivity.this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MarketActivity.this, view, "goodstransition").toBundle());
                } else {
                    startActivity(GoodsActivity.class, mBundle);
                }

            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
        getGoodsList(1);
    }


    public void getGoodsList(final int pagenum) {
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 333:
                CURPage = 0;
                Goodslist = new ArrayList<>();
                isRefresh = true;
                getGoodsList(1);
                break;
            default:
                break;

        }
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_menu})
    public void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.imgbtn_toolbar_back:
                    finish();
                    break;
                case R.id.imgbtn_toolbar_menu:
                    showMenuWindows(imageButton);
                    break;
            }
        }
    }

    protected PopupWindow weekListWindow;
    protected View popupWindowLayout;


    private void showMenuWindows(View parent) {
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupWindowLayout = layoutInflater.inflate(R.layout.popup_list_choose, null);

            TextView tvMime = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_mime);
            TextView tvAdd = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_add);
            tvAdd.setText("发布商品");
            tvMime.setText("我的发布");
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AddGoodsActivity.class);

                }
            });
            tvMime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MyGoodsActivity.class);

                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, DensityUtils.dp2px(MarketActivity.this, 170),
                    DensityUtils.dp2px(MarketActivity.this, 110));
        }

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        weekListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        weekListWindow.setFocusable(true);
        //设置点击外部可消失
        weekListWindow.setOutsideTouchable(true);
        weekListWindow.setBackgroundDrawable(new BitmapDrawable());
        weekListWindow.showAsDropDown(parent, -DensityUtils.dp2px(MarketActivity.this, 115), 20);
    }

}
