package com.dim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 普通的对话框，比代一个按钮，默认确定，第二个按钮默认不显示 中间文本区域如果不满全屏，那么显示对应的大小，如果超过全屏，那么可滑动
 * <p>
 * Created by dim on 2017/1/12 14:16 邮箱：271756926@qq.com
 */

public class DimAlertDialog extends Dialog {
    private Context                 mContext;
    // dialog的标题
    private TextView                dialogTitle;
    // dialog的显示文本
    private TextView                dialogMessage;
    // dialog的左边按钮，默认取消按钮
    private Button                  dialogCancle;
    // dialog的右边按钮，默认确定按钮
    private Button                  dialogComfire;
    // 整个布局的根结点
    private RelativeLayout          mRootView;
    // 左边按钮的点击事件监听，默认取消
    private OnKLMAlertClickListener mCancelClickListener;
    // 右边按钮的点击事件监听，默认确定
    private OnKLMAlertClickListener mConfirmClickListener;

    // 文案显示的位置，默认剧中
    private int                     mGravtity    = Gravity.CENTER;
    // 当次设置的标题
    private String                  mTitleText;
    // 当次设置的内容
    private String                  mContentText;
    // 当次设置的左边按钮的文案
    private String                  mCancelText;
    // 当次设置的右边按钮的文案
    private String                  mConfirmText;
    // 确认按钮的背景
    private Drawable                mConfirmDrawable;
    // 取消按钮的背景
    private Drawable                mCancleDrawable;
    // 是否需要显示文案内容
    private boolean                 mShowMessage = true;
    // 是否需要显示取消按钮
    private boolean                 mShowCancel  = true;
    // 屏幕的高度
    private int                     mScreenHeight;
    // 屏幕的宽度
    private int                     mScreenWid;

    public DimAlertDialog(Context context, int screenHigh, int screenWid) {
        super(context, R.style.dim_dialog);
        this.mContext = context;
        this.mScreenHeight = screenHigh;
        this.mScreenWid = screenWid;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        showCancelButton(false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        mRootView = (RelativeLayout) findViewById(R.id.base_warning_root);
        dialogTitle = (TextView) findViewById(R.id.base_warning_title);
        dialogMessage = (TextView) findViewById(R.id.base_warning_content);
        dialogCancle = (Button) findViewById(R.id.base_warning_btn_cancle);
        dialogCancle.setOnClickListener(e -> onCancleClick());
        dialogComfire = (Button) findViewById(R.id.base_warning_btn_comfire);
        dialogComfire.setOnClickListener(e -> onSureClick());

        dialogMessage.setMovementMethod(ScrollingMovementMethod.getInstance());

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setMessageContentShowType(mGravtity);
        if (mCancleDrawable != null) {
            setCancleButtonBg(mCancleDrawable);
        }
        if (mConfirmDrawable != null) {
            setConfirmButtonBg(mConfirmDrawable);
        }

        // 使得dialog的最大宽度不要填充满全屏幕
        int wid = mScreenWid - DimDialogUtils.DipToPixels(mContext, 40);
        // 获取当前的内容的高度
        ViewTreeObserver vto2 = dialogMessage.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialogMessage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // 文案高度
                int messageHei = dialogMessage.getHeight();
                // title布局设置的是50dip，按钮区域设置的title是50dip，为了不填充满屏幕高度，多设置了100dip的空白区域
                int leftViewHei = DimDialogUtils.DipToPixels(mContext, 200);
                // 设置view的宽高
                android.widget.FrameLayout.LayoutParams lp = new android.widget.FrameLayout.LayoutParams(wid, messageHei + leftViewHei / 2);
                // 如果内容的高度加上其他的高度已经超过屏幕的高度，那么设置内容区域的最大高度
                if (messageHei + leftViewHei > mScreenHeight) {
                    lp = new android.widget.FrameLayout.LayoutParams(wid, mScreenHeight - leftViewHei / 2);
                }
                mRootView.setLayoutParams(lp);
                // 设置内容区域的topmarging
                RelativeLayout.LayoutParams msgLp = new RelativeLayout.LayoutParams(wid, lp.height - leftViewHei / 2);
                msgLp.topMargin = DimDialogUtils.DipToPixels(mContext, 50);
                dialogMessage.setLayoutParams(msgLp);
            }
        });
    }

    /**
     * 方法的功能描述 设置dialog的title，如果设置为空，那么不显示title
     *
     * @author dim.
     * @time 2017/1/16 11:09.
     */
    public DimAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (dialogTitle != null && dialogTitle != null) {
            dialogTitle.setText(mTitleText);
            if (TextUtils.isEmpty(mTitleText)) {
                dialogTitle.setVisibility(View.GONE);
            } else {
                dialogTitle.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    /**
     * 方法的功能描述 设置dialog的文本内容，高度自适应，如果超出屏幕高度，则可滑动
     *
     * @author dim.
     * @time 2017/1/16 11:09.
     */
    public DimAlertDialog setContentText(String text) {
        mContentText = text;
        if (dialogMessage != null && mContentText != null) {
            showMessageVisiable(true);
            dialogMessage.setText(mContentText);
        }
        return this;
    }

    /**
     * 方法的功能描述 是否需要显示文本内容
     *
     * @author dim.
     * @time 2017/1/16 11:10.
     */
    public DimAlertDialog showMessageVisiable(boolean isShow) {
        mShowMessage = isShow;
        if (dialogMessage != null) {
            dialogMessage.setVisibility(mShowMessage ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    /**
     * 方法的功能描述 设置左边取消的显示文案
     *
     * @author dim.
     * @time 2017/1/16 11:10.
     */
    public DimAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (dialogCancle != null && mCancelText != null) {
            if (TextUtils.isEmpty(mCancelText)) {
                showCancelButton(false);
            } else {
                showCancelButton(true);
            }
            dialogCancle.setText(mCancelText);
        }
        return this;
    }

    /**
     * 方法的功能描述 设置右边按钮的显示文案
     *
     * @author dim.
     * @time 2017/1/16 11:11.
     */
    public DimAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (dialogComfire != null && mConfirmText != null) {
            dialogComfire.setText(mConfirmText);
        }
        return this;
    }

    /**
     * 方法的功能描述 设置是否显示左边的按钮（默认取消）
     *
     * @author dim.
     * @time 2017/1/16 11:11.
     */
    public DimAlertDialog showCancelButton(boolean isShow) {
        mShowCancel = isShow;
        if (dialogCancle != null) {
            dialogCancle.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    /**
     * 方法的功能描述 设置左边按钮的点击事件，默认取消
     *
     * @author dim.
     * @time 2017/1/16 11:11.
     */
    public DimAlertDialog setCancelClickListener(OnKLMAlertClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    /**
     * 方法的功能描述 设置右边按钮的点击事件，默认确定
     *
     * @author dim.
     * @time 2017/1/16 11:12.
     */
    public DimAlertDialog setConfirmClickListener(OnKLMAlertClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    /**
     * 方法的功能描述 底部两个按钮的点击事件
     *
     * @author dim.
     * @time 2017/1/16 11:12.
     */
    public static interface OnKLMAlertClickListener {
        public void onClick(DimAlertDialog sweetAlertDialog);
    }

    /**
     * 方法的功能描述 底部左边按钮的点击，默认取消
     *
     * @author dim.
     * @time 2017/1/16 11:12.
     */
    private void onCancleClick() {
        if (mCancelClickListener != null) {
            mCancelClickListener.onClick(DimAlertDialog.this);
        }
        DimAlertDialog.super.dismiss();
    }

    /**
     * 方法的功能描述 底部右边按钮的点击，默认确定
     *
     * @author dim.
     * @time 2017/1/16 11:13.
     */
    private void onSureClick() {
        if (mConfirmClickListener != null) {
            mConfirmClickListener.onClick(DimAlertDialog.this);
        }
        DimAlertDialog.super.dismiss();
    }

    /**
     * 方法的功能描述 设置消息内容显示的对齐方式，默认居中 参数为系统的Gravity.LEFT 这类数值
     *
     * @author dim.
     * @time 2017/1/16 11:45.
     */
    public DimAlertDialog setMessageContentShowType(int gravtity) {
        mGravtity = gravtity;
        if (dialogMessage != null) {
            dialogMessage.setGravity(mGravtity);
        }
        return this;
    }

    /**
     * 方法的功能描述 设置确认按钮的背景色
     *
     * @author dim.
     * @time 2017/1/23 10:48.
     */
    public DimAlertDialog setConfirmButtonBg(Drawable drawable) {
        mConfirmDrawable = drawable;
        if (dialogComfire != null && drawable != null) {
            dialogComfire.setBackground(drawable);
        }
        return this;
    }

    /**
     * 方法的功能描述 取消按钮的背景色
     *
     * @author dim.
     * @time 2017/1/23 10:50.
     */
    public DimAlertDialog setCancleButtonBg(Drawable drawable) {
        mCancleDrawable = drawable;
        if (dialogCancle != null && drawable != null) {
            dialogCancle.setBackground(drawable);
        }
        return this;
    }
}
