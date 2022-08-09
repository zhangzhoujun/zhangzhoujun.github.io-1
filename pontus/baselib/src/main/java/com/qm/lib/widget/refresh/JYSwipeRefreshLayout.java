package com.qm.lib.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author dim
 * @create at 2019/3/15 13:44
 * @description:
 */
public class JYSwipeRefreshLayout extends QRefreshLayout {

    public JYSwipeRefreshLayout(Context context) {
        super(context);
    }

    public JYSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void finishLoadmore() {
        setLoading(false);
    }

    public void finishRefresh() {
        setRefreshing(false);
    }

    public void setLoadmoreEnable(boolean canLoadMore) {
        setLoadEnable(canLoadMore);
    }

    public void setJYSwipeRefreshLayoutListener(JYSwipeRefreshLayoutListener mListener) {

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad() {
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        });
    }

    public interface JYSwipeRefreshLayoutListener {

        void onRefresh();

        void onLoadMore();

    }
}
