package com.qy.login.widget.input;

import androidx.databinding.BindingAdapter;

import com.dim.library.binding.command.BindingCommand;


/**
 * 文件描述：UserInput 的一些方法
 * 作者：dim
 * 创建时间：2019-08-01
 * 更改时间：2019-08-01
 * 版本号：1
 */

public class ViewAdapter {

    /**
     * @param gosInput UserInput 控件对象
     * @param inputOptions UserInput 输入的内容
     * @param getSmsClick UserInput 右边获取短信的点击
     */
    @BindingAdapter(value = { "userInputOptions", "getSmsClick", "getLeftClick" }, requireAll = false)
    public static void setGosInput(UserInput gosInput, InputOption inputOptions, final BindingCommand getSmsClick,
        final BindingCommand getLeftClick) {
        gosInput.setOptions(inputOptions);

        gosInput.setListener(() -> {
            if (getSmsClick != null) {
                getSmsClick.execute();
            }
        });

        gosInput.setLeftListener(() -> {
            if (getLeftClick != null) {
                getLeftClick.execute();
            }
        });
    }
}
