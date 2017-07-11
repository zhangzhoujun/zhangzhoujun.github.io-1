package com.kalemao.library.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;

/**
 * android中使用iconfont
 *
 */
public class KLMEduSohoIconEditText extends AppCompatEditText {

    private Context mContext;

    public KLMEduSohoIconEditText(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public KLMEduSohoIconEditText(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}
