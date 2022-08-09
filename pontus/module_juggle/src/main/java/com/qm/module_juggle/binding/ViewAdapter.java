package com.qm.module_juggle.binding;

import android.graphics.Paint;

import androidx.databinding.BindingAdapter;

import com.qm.lib.widget.dim.AppTextView;

/**
 * @author dim
 * @create at 2019/3/22 09:50
 * @description:
 */
public final class ViewAdapter {

    @BindingAdapter(value = {"strike_thru"}, requireAll = false)
    public static void setAppTextStrike(AppTextView textview, boolean strike_thru) {
        if (strike_thru) {
            textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }
}
