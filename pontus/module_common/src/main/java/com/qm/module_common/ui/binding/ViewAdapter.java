package com.qm.module_common.ui.binding;

import androidx.databinding.BindingAdapter;

import com.qm.lib.utils.JYComConst;
import com.qm.lib.widget.dim.AppImageView;
import com.qm.module_common.R;

/**
 * @ClassName ViewAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/20 11:48 AM
 * @Version 1.0
 */
public class ViewAdapter {

    @BindingAdapter(value = {"resoultType"}, requireAll = false)
    public static void setAppImage(AppImageView imageView, int resoultType) {
        if (resoultType == JYComConst.COMMON_RESULT_TYPE_SUCCESS) {
            imageView.setImageUrl(R.mipmap.common_resoult_success);
        } else if (resoultType == JYComConst.COMMON_RESULT_TYPE_FAIL) {
            imageView.setImageUrl(R.mipmap.common_resoult_fail);
        } else if (resoultType == JYComConst.COMMON_RESULT_TYPE_DOING) {
            imageView.setImageUrl(R.mipmap.common_resoult_doing);
        } else {
            imageView.setImageUrl(R.mipmap.common_resoult_success);
        }
    }
}
