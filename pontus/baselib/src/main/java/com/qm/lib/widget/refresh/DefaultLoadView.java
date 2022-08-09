package com.qm.lib.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.qm.lib.R;
import com.qm.lib.widget.dim.AppTextView;

/**
 * Created by zhangxiaoqi on 2019/4/17.
 */

public class DefaultLoadView extends LoadView {
    private AppTextView tvContent;

    public DefaultLoadView(Context context) {
        this(context, null);
    }

    public DefaultLoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.lib_base_view_default_foot, null);
        addView(view);

        tvContent = (AppTextView) view.findViewById(R.id.name);
    }

    @Override
    public void setHeight(float dragDistance, float distanceToRefresh, float totalDistance) {

    }

    @Override
    public void setRefresh() {
        tvContent.setText("正在加载更多");
    }

    @Override
    public void setPullToRefresh() {
        tvContent.setText("上拉加载更多");
    }

    @Override
    public void setReleaseToRefresh() {
        tvContent.setText("释放加载更多");
    }
}
