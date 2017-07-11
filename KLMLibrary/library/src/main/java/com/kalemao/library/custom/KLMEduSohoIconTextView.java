package com.kalemao.library.custom;

import com.kalemao.library.R;
import com.kalemao.library.logutils.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;

/**
 * android中使用iconfont
 *
 */
public class KLMEduSohoIconTextView extends AppCompatTextView {

    private Context mContext;
    private boolean mTipOn = false;

    private Dot     mDot;

    private class Dot {

        int color;

        int radius;

        int marginTop;
        int marginRight;

        Dot() {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);
            marginTop = (int) (9 * density);
            marginRight = (int) (9 * density);

            color = getContext().getResources().getColor(R.color.klm_red);
        }

    }

    public KLMEduSohoIconTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public KLMEduSohoIconTextView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
        mDot = new Dot();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTipOn) {
            float cx = getWidth() - mDot.marginRight - mDot.radius;
            float cy = mDot.marginTop + mDot.radius;

            Drawable drawableTop = getCompoundDrawables()[1];
            if (drawableTop != null) {
                int drawableTopWidth = drawableTop.getIntrinsicWidth();
                if (drawableTopWidth > 0) {
                    int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                    cx = dotLeft + mDot.radius;
                }
            }

            Paint paint = getPaint();
            // save
            int tempColor = paint.getColor();

            paint.setColor(mDot.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, mDot.radius, paint);

            // restore
            paint.setColor(tempColor);
        }
    }

    public void setTipOn(boolean tip) {
        this.mTipOn = tip;
        invalidate();
    }

    public boolean isTipOn() {
        return mTipOn;
    }
}
