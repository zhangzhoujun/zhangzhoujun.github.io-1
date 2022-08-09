package com.qm.lib.widget.toolbar;

import android.text.TextUtils;

import androidx.databinding.BindingAdapter;

import com.dim.library.binding.command.BindingCommand;
/**
 * @author dim
 * @create at 2019/3/13 17:16
 * @description:
 */
public final class ViewAdapter {

    @BindingAdapter(value = {
        "toolbarOptions", "onBackClick", "onRightClick", "onRightLeftClick", "onCloseClick"
    }, requireAll = false)
    public static void setToolbar(JYToolbar toolbar, JYToolbarOptions toolbarOptions, final BindingCommand backClick,
                                  final BindingCommand rightClick
        , final BindingCommand rightLeftClick, final BindingCommand closeClick) {
        if (toolbarOptions == null) {
            return;
        }

        if (toolbarOptions.getBgColor() != 0) {
            toolbar.setBgColorChange(toolbarOptions.getBgColor());
        }

        if (toolbarOptions.getTitleColor() != 0) {
            toolbar.setTextColorChange(toolbarOptions.getTitleColor());
        }
        if (!TextUtils.isEmpty(toolbarOptions.getTitleString())) {
            toolbar.setTitleCenter(toolbarOptions.getTitleString());
        }
        if (!TextUtils.isEmpty(toolbarOptions.getRightString())) {
            toolbar.setRightText(toolbarOptions.getRightString());
            if (toolbarOptions.getRightStringColor() != 0) {
                toolbar.setRightTextColor(toolbarOptions.getRightStringColor());
            }
        } else {
            if (!TextUtils.isEmpty(toolbarOptions.getRightIconUrl())) {
                toolbar.setRightIconForWeb(toolbarOptions.getRightIconUrl());
            } else {
                toolbar.setRightIcon(toolbarOptions.getRightId());
                toolbar.setRightLeftIcon(toolbarOptions.getRightLeftId());
            }
        }
        if (toolbarOptions.getBackId() != 0) {
            toolbar.setBackIcon(toolbarOptions.getBackId());
        }
        toolbar.setBackVisibility(toolbarOptions.isNeedNavigate());

        toolbar.setCloseVisibility(toolbarOptions.isDoesShowClose());

        toolbar.getBackIcon().setOnClickListener(v -> {
            if (backClick != null) {
                backClick.execute();
            }
        });

        toolbar.getRightIcon().setOnClickListener(v -> {
            if (rightClick != null) {
                rightClick.execute();
            }
        });

        toolbar.getRightLeftIcon().setOnClickListener(v -> {
            if (rightLeftClick != null) {
                rightLeftClick.execute();
            }
        });

        toolbar.getCloseIcon().setOnClickListener(v -> {
            if (closeClick != null) {
                closeClick.execute();
            }
        });
    }
}
