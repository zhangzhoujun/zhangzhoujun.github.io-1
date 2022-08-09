package com.qm.lib.widget.dim;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * @author dim
 * @create at 2019/3/21 15:10
 * @description:
 */
public class HomeTextView extends AppTextView {

    public HomeTextView(Context context) {
        super(context);
        initView(context);
    }

    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "home.TTF");
        setTypeface(iconfont);
    }
}
