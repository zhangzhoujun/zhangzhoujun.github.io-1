package com.kalemao.library.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;

/**
 * android中使用iconfont
 *
 */
public class KLMEduSohoIconButton extends AppCompatButton {

    private Context mContext;

    public KLMEduSohoIconButton(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public KLMEduSohoIconButton(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}
