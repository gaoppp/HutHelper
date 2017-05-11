package com.gaop.huthelper.view.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.view.adapter.VideoListRVAdapter;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.entity.VideoData;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop1 on 2017/4/9.
 */

public class VideoListActivity extends BaseActivity {
    private static final String TAG = "VideoListActivity";
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_videolist)
    LRecyclerView rvVideolist;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    VideoData data = new VideoData();

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_videolist;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("视频专栏");
        rvVideolist.setEmptyView(rlEmpty);
        getVideoData();
    }


    public void getVideoData() {

        SubscriberOnNextListener getVideoData = new SubscriberOnNextListener<VideoData>() {
            @Override
            public void onNext(VideoData o) {
                if ("ok".equals(o.getMsg())) {
                    data = o;
                    rvVideolist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    mLRecyclerViewAdapter = new LRecyclerViewAdapter(VideoListActivity.this, new VideoListRVAdapter(VideoListActivity.this, o.getLinks(), o.get_$480P()));
                    rvVideolist.setAdapter(mLRecyclerViewAdapter);
                    rvVideolist.setLScrollListener(new LRecyclerView.LScrollListener() {
                        @Override
                        public void onRefresh() {
                            getVideoData();
                        }

                        @Override
                        public void onScrollUp() {
                        }

                        @Override
                        public void onScrollDown() {

                        }

                        @Override
                        public void onBottom() {
                            RecyclerViewStateUtils.setFooterViewState(rvVideolist, LoadingFooter.State.TheEnd);
                        }

                        @Override
                        public void onScrolled(int distanceX, int distanceY) {
                        }
                    });
                    mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            Bundle mBundle = new Bundle();
                            mBundle.putString("url", data.get_$480P());
                            mBundle.putSerializable("link", data.getLinks().get(i));

                            if (Build.VERSION.SDK_INT > 20) {
                                Intent intent = new Intent(VideoListActivity.this, VideoItemActivity.class);
                                intent.putExtras(mBundle);
                                VideoListActivity.this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(VideoListActivity.this, view, "videotransition").toBundle());
                            } else {
                                startActivity(GoodsActivity.class, mBundle);
                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int i) {
                        }
                    });
                } else
                    ToastUtil.showToastShort(o.getMsg());
                rvVideolist.refreshComplete();
                // mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        HttpMethods.getInstance().getVideoData(
                new ProgressSubscriber<VideoData>(getVideoData, VideoListActivity.this));
    }

    @OnClick(R.id.imgbtn_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
