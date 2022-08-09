package com.gos.nodetransfer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.gos.nodetransfer.R;
import com.qm.lib.widget.dim.AppImageView;

import java.util.concurrent.ExecutionException;

import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

/**
 * @author dim
 * @create at 2019/4/3 17:22
 * @description:
 */
public class QmNormalItemView extends NormalItemView {

    private AppImageView mIcon;
    private final RoundMessageView mMessages;

    private String mDefaultDrawable;
    private String mCheckedDrawable;

    private boolean mChecked;

    private Context mContext;

    public QmNormalItemView(Context context) {
        this(context, null);
    }

    public QmNormalItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QmNormalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.qm_main_bottom_item, this, true);

        mIcon = findViewById(R.id.bottom_icon);
        mMessages = findViewById(R.id.bottom_messages);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return QmNormalItemView.class.getName();
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     */
    public void initialize(String drawableRes, String checkedDrawableRes) {
        mDefaultDrawable = drawableRes;
        mCheckedDrawable = checkedDrawableRes;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIcon.setImageUrl(mCheckedDrawable);
        } else {
            mIcon.setImageUrl(mDefaultDrawable);
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

    }

    @Override
    public void setDefaultDrawable(Drawable drawable) {
        if (!mChecked) {
            mIcon.setImageUrl(drawable);
        }
    }

    @Override
    public void setSelectedDrawable(Drawable drawable) {
        if (mChecked) {
            mIcon.setImageUrl(drawable);
        }
    }

    @Override
    public String getTitle() {
        return "";
    }


}
