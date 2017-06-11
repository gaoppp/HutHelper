package com.gaop.huthelper.view.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
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
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.widget.RichText;
import com.gaop.huthelper.view.adapter.LoseListAdapter;
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
 * 失物招领列表Activity
 * Created by 高沛 on 16-11-10.
 */

public class LoseListActivity extends BaseActivity {

    @BindView(R.id.rv_loselist)
    LRecyclerView rvLoselist;
    @BindView(R.id.imgbtn_toolbar_menu)
    ImageButton ivMenu;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    private int COUNT = 0;
    private int CURPage = 0;

    private List<Lose> loseList = new ArrayList<>();

    LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_lose_new;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("失物招领");
        rvLoselist.setEmptyView(rlEmpty);
        getLostList(1);
        rvLoselist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, new LoseListAdapter(LoseListActivity.this, loseList));
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lose", loseList.get(i));

                //动画处理
                if (Build.VERSION.SDK_INT > 20) {
                    Intent intent = new Intent(LoseListActivity.this, LoseActivity.class);
                    intent.putExtras(bundle);
                    LoseListActivity.this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoseListActivity.this, view, "losetransition").toBundle());
                } else {
                    startActivity(LoseActivity.class, bundle);
                }

            }

            @Override
            public void onItemLongClick(View view, int i) {
            }
        });

        rvLoselist.setAdapter(mLRecyclerViewAdapter);
        rvLoselist.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                CURPage = 0;
                loseList.clear();
                getLostList(1);
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
                    getLostList(CURPage);
                } else
                    RecyclerViewStateUtils.setFooterViewState(rvLoselist, LoadingFooter.State.TheEnd);
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 333:
                CURPage = 0;
                loseList.clear();
                getLostList(1);
                rvLoselist.scrollToPosition(0);
                rvLoselist.forceToRefresh();
                break;
            default:
                break;

        }
    }


    private void getLostList(final int pagenum) {
        SubscriberOnNextListener<HttpResult<PageData<Lose>>> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult<PageData<Lose>>>() {
            @Override
            public void onNext(HttpResult<PageData<Lose>> o) {
                if (o.getMsg().equals("ok")) {
                    COUNT = o.getData().getInfo().getPage_max();
                    CURPage = Integer.valueOf(o.getData().getInfo().getPage_cur());
                    loseList.addAll(o.getData().getPosts());
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToastShort("获取服务器数据为空");
                }
                rvLoselist.refreshComplete();
                mLRecyclerViewAdapter.notifyDataSetChanged();

            }
        };
        HttpMethods.getInstance().getLose(
                new ProgressSubscriber<HttpResult<PageData<Lose>>>(subscriberOnNextListener, LoseListActivity.this)
                , pagenum);
    }


    protected PopupWindow weekListWindow;
    protected View popupWindowLayout;

    /**
     * 显示PopupWindows
     *
     * @param parent 相对位置View
     */
    private void showMenuWindows(View parent) {
        int width = ScreenUtils.getScreenWidth(LoseListActivity.this) / 2;
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupWindowLayout = layoutInflater.inflate(R.layout.popup_list_choose, null);

            RichText tvMime = (RichText) popupWindowLayout.findViewById(R.id.tv_popmenu_mime);
            RichText tvAdd = (RichText) popupWindowLayout.findViewById(R.id.tv_popmenu_add);
            tvAdd.setText("添加失物");
            tvMime.setText("我的发布");
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AddLoseActivity.class);

                }
            });
            tvMime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MyLoseActivity.class);

                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, DensityUtils.dp2px(LoseListActivity.this, 170),
                    DensityUtils.dp2px(LoseListActivity.this, 110));
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
        weekListWindow.showAsDropDown(parent, -DensityUtils.dp2px(LoseListActivity.this, 115), 20);
    }

    @OnClick({R.id.imgbtn_toolbar_back, R.id.imgbtn_toolbar_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                finish();
                break;
            case R.id.imgbtn_toolbar_menu:
                showMenuWindows(ivMenu);
                break;
        }
    }

}
