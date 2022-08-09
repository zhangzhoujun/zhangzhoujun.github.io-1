package com.qm.lib.widget.dim;

import android.content.Context;
import android.util.AttributeSet;

import com.qm.lib.R;

/**
 * 文件描述：全民数据链 通用的按钮
 * 作者：dim
 * 创建时间：2019-07-30
 * 更改时间：2019-07-30
 * 版本号：1
 */
public class DimButton extends AppButton {

    public DimButton(Context context) {
        super(context);
        initNodeButton();
    }

    public DimButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNodeButton();
    }

    public DimButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNodeButton();
    }

    private void initNodeButton() {

    }

    public void setCanClick(boolean canClick) {
        if (canClick) {
            setClickable(true);
            setTextColor(getContext().getResources().getColor(R.color.colorBaseWhite));
//            setBackgroundResource(R.drawable.lib_base_node_button_selector);
            setBackgroundResource(R.drawable.lib_base_node_button);
        } else {
            setClickable(false);
            setTextColor(getContext().getResources().getColor(R.color.colorBaseWhite));
//            setBackgroundResource(R.drawable.lib_base_node_button_un);
            setBackgroundResource(R.drawable.lib_base_node_button_click);
        }
    }
}
