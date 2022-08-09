package com.qm.module_juggle.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @ClassName QYGridLayoutManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 12/18/20 11:45 AM
 * @Version 1.0
 */
public class QYGridLayoutManager extends GridLayoutManager {

    private boolean canScroll = true;
    private boolean groupScrollToBottom = true;

    public QYGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public QYGridLayoutManager(Context context, int spanCount, boolean groupToBottom) {
        super(context, spanCount);
        groupScrollToBottom = groupToBottom;
    }

    public QYGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * 垂直方向
     *
     * @return
     */
    @Override
    public boolean canScrollVertically() {
//        return helper.isStickyNow() && super.canScrollVertically();
        return groupScrollToBottom && super.canScrollVertically();
//        return canScroll && super.canScrollVertically();
//        return !groupLM.canScrollVertically();
    }

    /**
     * 水平方向
     *
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

    /**
     * 设置是否可以滑动
     *
     * @param canScroll
     */
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public void setScrollBottom(boolean canScroll) {
        this.groupScrollToBottom = canScroll;
    }
}
