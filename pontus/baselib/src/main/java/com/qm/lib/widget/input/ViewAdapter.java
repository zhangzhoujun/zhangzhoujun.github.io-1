package com.qm.lib.widget.input;

import androidx.databinding.BindingAdapter;

import com.dim.library.binding.command.BindingCommand;

/**
 * @author dim
 * @create at 2019/3/22 09:50
 * @description:
 */
public final class ViewAdapter {

    @BindingAdapter(value = {"jyInputOptions", "getSmsClick"}, requireAll = false)
    public static void setJYInput(AppInput jyInput, JYInputOptions JYInputOptions, final BindingCommand getSmsClick) {
        jyInput.setOptions(JYInputOptions);

        jyInput.setListener(new AppInput.onGetSmsClickCallBack() {
            @Override
            public void ongetSmsClick() {
                if (getSmsClick != null) {
                    getSmsClick.execute();
                }
            }
        });
    }

}
