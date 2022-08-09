package com.qm.lib.widget.toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.dim.library.utils.ConvertUtils;
import com.dim.library.utils.DLog;
import com.qm.lib.R;
import com.qm.lib.widget.dim.AppImageView;
import com.qm.lib.widget.dim.AppTextView;

/**
 * @author dim
 * @create at 2019/3/12 11:35
 * @description:
 */
public class JYToolbar extends Toolbar {

    private Toolbar mToolbar;
    private AppTextView mTitle;
    private AppTextView mBackText;
    private AppImageView mBack;
    private RelativeLayout mBackLayout;

    private AppTextView mRightText;
    private RelativeLayout mCloseText;

    private AppImageView mRight;
    private LinearLayout mRightLayout;

    private AppImageView mRightLeft;
    private RelativeLayout mRightLeftLayout;

    private RelativeLayout mRootView;

    private int mTextColor;
    private int mToolbarColor;

    public JYToolbar(@NonNull Context context) {
        this(context, null);
        init(context);
    }

    public JYToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public JYToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lib_base_toolbar, this);
        mRootView = findViewById(R.id.root);
        mToolbar = findViewById(R.id.gos_toolbar);
        mTitle = findViewById(R.id.gos_toolbar_title);
        mBack = findViewById(R.id.gos_toolbar_back);
        mCloseText = findViewById(R.id.gos_toolbar_close_layout);
        mBackText = findViewById(R.id.gos_toolbar_back_text);
        mBackLayout = findViewById(R.id.gos_toolbar_back_layout);
        mRight = findViewById(R.id.gos_toolbar_right);
        mRightLayout = findViewById(R.id.gos_toolbar_right_layout);
        mRightLeft = findViewById(R.id.gos_toolbar_2_right);
        mRightLeftLayout = findViewById(R.id.gos_toolbar_Right_2_layout);
        mRightText = findViewById(R.id.gos_toolbar_right_text);
        mTextColor = context.getResources().getColor(R.color.colorBaseBg);
        mToolbarColor = context.getResources().getColor(R.color.node_bg);

        mTitle.setTextColor(mTextColor);
        mRightText.setTextColor(mTextColor);
        mBackText.setTextColor(mTextColor);
        //        mRootView.setBackgroundColor(mToolbarColor);
    }

    public void setColorChange(int textColor, int bgColor) {
        setTextColorChange(textColor);
        setBgColorChange(bgColor);
    }

    public void setTextColorChange(int textColor) {
        mTextColor = textColor;
        mTitle.setTextColor(textColor);
        mRightText.setTextColor(textColor);
        mBackText.setTextColor(textColor);
    }

    public RelativeLayout getCloseIcon() {
        return mCloseText;
    }

    public void setCloseVisibility(boolean doesShow) {
        if (doesShow) {
            mCloseText.setVisibility(View.VISIBLE);
        } else {
            mCloseText.setVisibility(View.INVISIBLE);
        }
    }

    public void setBgColorChange(int bgColor) {
        if (bgColor == -1) {
            return;
        }
        mToolbarColor = bgColor;
        try {
            DLog.d("设置背景色： " + bgColor);
            mToolbar.setBackgroundResource(bgColor);
        } catch (Exception e) {
            e.printStackTrace();
            mToolbar.setBackgroundColor(bgColor);
        }
    }

    public void setTitleCenter(String title) {
        mTitle.setTextColor(mTextColor);
        mTitle.setText(title);
    }

    public void setBackVisibility(boolean doesShow) {
        if (doesShow) {
            mBackLayout.setVisibility(View.VISIBLE);
        } else {
            mBackLayout.setVisibility(View.INVISIBLE);
        }
    }

    public RelativeLayout getBackIcon() {
        return mBackLayout;
    }

    public LinearLayout getRightIcon() {
        return mRightLayout;
    }

    public RelativeLayout getRightLeftIcon() {
        return mRightLeftLayout;
    }

    @SuppressLint("ResourceType")
    public void setTransparentBg() {
        mToolbar.setBackgroundResource(getContext().getResources().getColor(R.color.transparent));
    }

    public void setBackIcon(int resId) {
        if (resId != 0) {
            mBack.setBackgroundResource(resId);
            mBackLayout.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.VISIBLE);
            mBackText.setVisibility(View.GONE);
        } else {
            mBackLayout.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.INVISIBLE);
            mBackText.setVisibility(View.VISIBLE);
        }
    }

    public void setRightIcon(int resId) {
        if (resId != 0) {
            mRight.setBackgroundResource(resId);
            mRight.setVisibility(View.VISIBLE);
            mRightText.setVisibility(View.GONE);
            mRightLayout.setVisibility(View.VISIBLE);
        } else {
            mRightLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightIconForWeb(String resId) {
        if (!TextUtils.isEmpty(resId)) {
            // 多次加载图片引起大小问题
            // 暂时无法解决这个问题，暂时这么处理
            int hei = mRightLayout.getHeight();
            int wid = mRightLayout.getWidth();
            if (wid != 0 && hei != 0) {
                ViewGroup.LayoutParams lp = mRight.getLayoutParams();
                lp.width = wid;
                lp.height = hei;
                mRight.setLayoutParams(lp);
            }
            mRight.setImageUrl(resId);
            mRight.setVisibility(View.VISIBLE);
            mRightText.setVisibility(View.GONE);
            mRightLayout.setVisibility(View.VISIBLE);
        } else {
            mRightLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightIcon(String resId) {
        if (!TextUtils.isEmpty(resId)) {
            mRight.setImageUrl(resId);
            mRight.setVisibility(View.VISIBLE);
            mRightText.setVisibility(View.GONE);
            mRightLayout.setVisibility(View.VISIBLE);
        } else {
            mRightLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightLeftIcon(int resId) {
        if (resId != 0) {
            mRightLeft.setBackgroundResource(resId);
            mRightLeftLayout.setVisibility(View.VISIBLE);
        } else {
            mRightLeftLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightText(String rightText) {
        mRightText.setText(rightText);
        mRightText.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.VISIBLE);
        mRight.setVisibility(View.GONE);
    }

    public void setRightTextDrawable(int resid) {
        Drawable rightD = getResources().getDrawable(resid);
        rightD.setBounds(0, 0, rightD.getMinimumWidth(), rightD.getMinimumHeight());
        mRightText.setCompoundDrawables(null, null, rightD, null);
        mRightText.setCompoundDrawablePadding(ConvertUtils.dp2px(10));
    }

    public void setRightTextColor(int resId) {
        mRightText.setTextColor(resId);
    }
}
