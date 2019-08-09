package com.dim.dimpieview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.dim.dimpieview.databinding.DimPieViewBinding;

/**
 * @author dim
 * @create at 2019-05-23 15:59
 * @description:
 */
public class DimPieView extends RelativeLayout {

    private DimPieViewBinding binding;


    public DimPieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DimPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, R.layout.dim_pie_view, this, true);//第四个参数为true很重要，让我们的自定义View可以正常显示

    }
}
