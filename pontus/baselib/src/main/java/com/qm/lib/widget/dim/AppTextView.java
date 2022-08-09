package com.qm.lib.widget.dim;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author dim
 * @create at 2019/3/21 15:10
 * @description:
 */
public class AppTextView extends AppCompatTextView {

    public AppTextView(Context context) {
        super(context);
        initView(context);
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AppTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
//        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "gos_app.ttf");
//        setTypeface(iconfont);
    }
}
