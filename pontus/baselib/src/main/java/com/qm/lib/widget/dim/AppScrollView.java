package com.qm.lib.widget.dim;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author dim
 * @create at 2019-06-04 15:08
 * @description:
 */
public class AppScrollView extends ScrollView {

    private OnScrollListener listener;

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public AppScrollView(Context context) {
        super(context);
    }

    public AppScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置接口
    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

    //重写原生onScrollChanged方法，将参数传递给接口，由接口传递出去
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {

            //这里我只传了垂直滑动的距离
            listener.onScroll(t);
        }
    }
}