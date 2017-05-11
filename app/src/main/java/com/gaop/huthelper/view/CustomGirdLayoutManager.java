package com.gaop.huthelper.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by 高沛 on 2017/4/10.
 * 禁止滑动的RecycleView LayoutManager
 */

public class CustomGirdLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomGirdLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
