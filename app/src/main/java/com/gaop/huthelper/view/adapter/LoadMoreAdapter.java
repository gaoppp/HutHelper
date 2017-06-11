package com.gaop.huthelper.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.widget.RecycleOnScrollListener;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;

import rx.Single;

/**
 * Created by 高沛 on 2017/4/17.
 */

public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public LayoutInflater inflater;

    public ArrayList<T> dataList;

    private boolean showLoadMore = true;

    public final int TYPE_FOOTVIEW = 100;
    public FootViewHolder footViewHolder;

    public LoadMoreAdapterItemClickListener itemClickListener;
    private RecycleOnScrollListener onScrollListener;
    public LifecycleTransformer mTransformer;
    private RecyclerView mRecyclerView;

    private NotifyObserver mNotifyObserver;

    public void loadMore() {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        onScrollListener = new RecycleOnScrollListener(mRecyclerView) {
            @Override
            public void onLoadMore(int currentPage) {
                if (itemClickListener != null)
                    itemClickListener.loadMore();
            }
        };
        mRecyclerView.addOnScrollListener(onScrollListener);
        onScrollListener.setLoading(false);
        mNotifyObserver = new NotifyObserver();
        registerAdapterDataObserver(mNotifyObserver);

        if (dataList != null) {
            if (dataList.size() <= 3) {
                setShowLoadMore(false);
            } else {
                setShowLoadMore(true);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (onScrollListener != null) {
            mRecyclerView.removeOnScrollListener(onScrollListener);
        }
        onScrollListener = null;
        if (mNotifyObserver != null) {
            unregisterAdapterDataObserver(mNotifyObserver);
        }
        mNotifyObserver = null;
        mRecyclerView = null;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTVIEW) {
            footViewHolder = new FootViewHolder(inflater.inflate(R.layout.item_footview, parent,
                    false));
            return footViewHolder;
        } else {
            return createViewholder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_FOOTVIEW) {
            RxView.clicks(footViewHolder.ll_foot)
                    .compose(mTransformer)
                    .subscribe(new Single.OnSubscribe() {
                        @Override
                        public void call(Object o) {
                            if (itemClickListener != null) {
                                footViewHolder.showProgress();
                                itemClickListener.footViewClick();
                            }
                        }
                    });

        } else {
            bindHolder(viewHolder, position);
            if (viewHolder.itemView != null && itemClickListener != null)
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.itemClickLinstener(position);
                    }
                });
        }
    }


    protected abstract void bindHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder createViewholder(ViewGroup parent, int viewType);

    public void setOnItemClickListener(LoadMoreAdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return showLoadMore ? (dataList != null ? dataList.size() + 1 : 0) :
                (dataList != null ? dataList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadMore) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTVIEW;
            } else {
                return getViewType(position);
            }
        }
        return getViewType(position);

    }

    public void destroy() {
        if (onScrollListener != null) {
            mRecyclerView.removeOnScrollListener(onScrollListener);
        }
    }

    public LoadMoreAdapter(Context context,
                           ArrayList<T> dataList,
                           @NonNull LifecycleTransformer mTransformer) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(this.context);
        this.mTransformer = mTransformer;
    }


    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public abstract int getViewType(int position);

    public void setShowLoadMore(boolean showLoadMore) {
        this.showLoadMore = showLoadMore;
        setCanLoading(showLoadMore);
    }

    /**
     * 设置footShow显示
     */
    public void footShowClickText() {
        if (footViewHolder != null) {
            footViewHolder.showClickText();
        }
    }

    /**
     * 显示-已加载全部数据
     */
    public void showNoData() {
        if (footViewHolder != null) {
            footViewHolder.setClickable(false);
            footViewHolder.showText("已加载全部数据");
            setCanLoading(false);
        }
    }

    public void showLoading() {
        try {
            if (footViewHolder != null) {
                footViewHolder.setClickable(true);
                footViewHolder.showProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoading(boolean loading) {
        showLoading();
        setCanLoading(loading);
    }

    public void setCanLoading(boolean loading) {
        if (onScrollListener != null) {
            onScrollListener.setCanLoading(loading);
            onScrollListener.setLoading(!loading);
        }
    }

    public void footShowProgress() {
        if (footViewHolder != null) {
            footViewHolder.showProgress();
        }
    }

    public void setFootClickable(boolean clickable) {
        if (footViewHolder != null) {
            footViewHolder.setClickable(clickable);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_foot;
        private ProgressBar progressBar;
        private TextView tvLoad;
        private View itemView;

        public FootViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ll_foot = (LinearLayout) itemView.findViewById(R.id.ll_foot);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            tvLoad = (TextView) itemView.findViewById(R.id.tvLoad);
        }

        public void setClickable(boolean clickable) {
            ll_foot.setClickable(clickable);
        }

        public void setTvLoadText(String text) {
            tvLoad.setText(text);
        }

        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
            tvLoad.setText("加载中...");
        }

        public void showClickText() {
            progressBar.setVisibility(View.GONE);
            tvLoad.setText("点击重试");
        }

        public void showText(String text) {
            progressBar.setVisibility(View.GONE);
            setTvLoadText(text);
        }
    }

    class NotifyObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            if (onScrollListener != null) {
                onScrollListener.setLoading(false);
            }
            if (dataList != null) {
                if (dataList.size() <= 3) {
                    setShowLoadMore(false);
                } else {
                    setShowLoadMore(true);
                }
            }
        }
    }
}
