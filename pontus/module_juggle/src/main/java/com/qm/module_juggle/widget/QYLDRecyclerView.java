package com.qm.module_juggle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName QYLDRecyclerView
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 12/18/20 6:03 PM
 * @Version 1.0
 */
public class QYLDRecyclerView extends RecyclerView {
    private boolean groupScrollToBottom = true;
    private boolean scrollToTop = true;

    public QYLDRecyclerView(Context context) {
        super(context);
    }

    public QYLDRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QYLDRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float lastTouchY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //解决recyclerView和viewPager的滑动影响
        //当滑动recyclerView时，告知父控件不要拦截事件，交给子view处理
        if (groupScrollToBottom) {
            if (scrollToTop) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    lastTouchY = ev.getY();
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                    float disY = ev.getY() - lastTouchY;
                    if (disY < 0) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setGroupScrollBottom(boolean canScroll) {
        this.groupScrollToBottom = canScroll;
    }

    public void setScrollToTop(boolean canScroll) {
        this.scrollToTop = canScroll;
    }

    public boolean isGroupScrollToBottom() {
        return groupScrollToBottom;
    }

    public void setGroupScrollToBottom(boolean groupScrollToBottom) {
        this.groupScrollToBottom = groupScrollToBottom;
    }

    public boolean isScrollToTop() {
        return scrollToTop;
    }
}