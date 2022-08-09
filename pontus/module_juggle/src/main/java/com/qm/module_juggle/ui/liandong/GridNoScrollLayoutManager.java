package com.qm.module_juggle.ui.liandong;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @ClassName GridNoScrollLayoutManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/29 10:20 AM
 * @Version 1.0
 */
class GridNoScrollLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = false;

    public GridNoScrollLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
