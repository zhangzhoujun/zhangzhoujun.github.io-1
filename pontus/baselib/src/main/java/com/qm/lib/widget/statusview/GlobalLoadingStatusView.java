package com.qm.lib.widget.statusview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qm.lib.R;
import com.qm.lib.utils.SystemUtil;

/**
 * simple loading status view for global usage
 *
 * @author billy.qi
 * @since 19/3/19 23:12
 */
@SuppressLint("ViewConstructor")
public class GlobalLoadingStatusView extends LinearLayout implements View.OnClickListener {

    private final TextView mTextView;
    private final Runnable mRetryTask;
    private final ImageView mImageView;
    private String des;

    public GlobalLoadingStatusView(Context context, Runnable retryTask, String des) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.lib_base_global_loading, this, true);
        mImageView = findViewById(R.id.image);
        mTextView = findViewById(R.id.text);
        this.des = des;
        this.mRetryTask = retryTask;
        setBackgroundColor(0xFFF0F0F0);
    }

    public void setMsgViewVisibility(boolean visible) {
        mTextView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setStatus(int status) {
        boolean show = true;
        OnClickListener onClickListener = null;
        int image = R.drawable.lib_base_load_no_data;
        String str = getResources().getString(R.string.lib_base_load_str_none);
        switch (status) {
            case Gloading.STATUS_LOAD_SUCCESS:
                show = false;
                break;
            case Gloading.STATUS_LOADING:
                str = getResources().getString(R.string.lib_base_loading);
                break;
            case Gloading.STATUS_LOAD_FAILED:
                str = getResources().getString(R.string.lib_base_load_failed);
                image = R.drawable.lib_base_load_no_data;
                Boolean networkConn = SystemUtil.isNetworkConnected(getContext());
                if (networkConn != null && !networkConn) {
                    str = getResources().getString(R.string.lib_base_load_failed_no_network);
                    image = R.drawable.lib_base_load_no_net;
                }
                onClickListener = this;
                break;
            case Gloading.STATUS_EMPTY_DATA:
                str = TextUtils.isEmpty(des) ? getResources().getString(R.string.lib_base_load_empty) : des;
                image = R.drawable.lib_base_load_no_data;
                break;
            default:
                break;
        }
        mImageView.setImageResource(image);
        setOnClickListener(onClickListener);
        mTextView.setText(str);
        setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (mRetryTask != null) {
            mRetryTask.run();
        }
    }
}
