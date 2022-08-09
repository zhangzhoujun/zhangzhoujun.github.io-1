package com.qm.lib.widget.image;


import android.text.TextUtils;

import androidx.databinding.BindingAdapter;

import com.qm.lib.widget.dim.AppImageView;

/**
 * Created by  on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"imgIconUrl", "widHei", "lineCount"})
    public static void setImageUri(AppImageView imageView, String imgIconUrl, String widHei, int lineCount) {
        if (!TextUtils.isEmpty(imgIconUrl)) {
            imageView.setImageUrl(imgIconUrl, widHei, lineCount);
        }
    }
}

