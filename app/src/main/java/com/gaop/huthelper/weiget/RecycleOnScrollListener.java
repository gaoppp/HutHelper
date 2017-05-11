package com.gaop.huthelper.weiget;

import android.support.v7.widget.RecyclerView;

import com.gaop.huthelper.utils.RecycleViewPositonHelper;

/**
 * Created by 高沛 on 2017/4/21.
 */

public class RecycleOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;

    private boolean loading = true;

    private int currentPage = 1;
    private boolean shouldLoading = true;
    int lastCompletelyVisiableItemPosition, visibleItemCount, totalItemCount;

    private RecycleViewPositonHelper helper;

    public RecycleOnScrollListener(RecyclerView recyclerView) {

        helper = new RecycleViewPositonHelper(recyclerView);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (shouldLoading) {
            lastCompletelyVisiableItemPosition = helper.findLastCompletelyVisibleItemPosition();
            totalItemCount = helper.getItemCount();
            visibleItemCount = recyclerView.getChildCount();

            if(loading){
                if(totalItemCount>previousTotal){
                    loading=false;
                    previousTotal=totalItemCount;
                }
            }
            if(!loading&&(visibleItemCount>0)&&(lastCompletelyVisiableItemPosition>=totalItemCount-1)){
                currentPage++;
                onLoadMore(currentPage);
                loading=true;
            }
        }
    }
    public void setCanLoading(boolean loading) {
        shouldLoading = loading;
        if (true == shouldLoading)
            previousTotal = helper.getItemCount();
    }

    public void setLoading(boolean loading){
        this.loading = loading;
        if (true == loading)
            previousTotal = helper.getItemCount();
    }

    public void onLoadMore(int currentPage) {
    }
}
