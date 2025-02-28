package com.gos.nodetransfer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.gos.nodetransfer.R;

import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

/**
 * @author dim
 * @create at 2019/4/3 17:22
 * @description:
 */
public class JYNormalItemView extends NormalItemView {

    private ImageView mIcon;
    private final TextView mTitle;
    private final RoundMessageView mMessages;

    private Drawable mDefaultDrawable;
    private Drawable mCheckedDrawable;

    private int mDefaultTextColor = 0x56000000;
    private int mCheckedTextColor = 0x56000000;

    private boolean mChecked;

    public JYNormalItemView(Context context) {
        this(context, null);
    }

    public JYNormalItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JYNormalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.main_bottom_item, this, true);

        mIcon = findViewById(R.id.bottom_icon);
        mTitle = findViewById(R.id.bottom_title);
        mMessages = findViewById(R.id.bottom_messages);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return JYNormalItemView.class.getName();
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, String title) {
        mDefaultDrawable = ContextCompat.getDrawable(getContext(), drawableRes);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), checkedDrawableRes);
        mTitle.setText(title);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable);
            mTitle.setTextColor(mCheckedTextColor);
        } else {
            mIcon.setImageDrawable(mDefaultDrawable);
            mTitle.setTextColor(mDefaultTextColor);
        }
        mChecked = checked;
    }

    @Override
    public void setMessageNumber(int number) {
        mMessages.setMessageNumber(number);
    }

    public int getMessageNum() {
        return mMessages.getMessageNumber();
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
        mMessages.setHasMessage(hasMessage);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDefaultDrawable(Drawable drawable) {
        mDefaultDrawable = drawable;
        if (!mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public void setSelectedDrawable(Drawable drawable) {
        mCheckedDrawable = drawable;
        if (mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void setTextDefaultColor(@ColorInt int color) {
        mDefaultTextColor = color;
    }

    public void setTextCheckedColor(@ColorInt int color) {
        mCheckedTextColor = color;
    }

}
