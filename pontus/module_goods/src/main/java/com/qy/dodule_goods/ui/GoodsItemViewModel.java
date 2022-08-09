package com.qy.dodule_goods.ui;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dim.library.base.ItemViewModel;

/**
 * @ClassName GoodsViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/9 1:50 PM
 * @Version 1.0
 */
public class GoodsItemViewModel extends ItemViewModel<GoodsViewModel> {

    public ObservableField<String> item = new ObservableField<>();

    public GoodsItemViewModel(@NonNull GoodsViewModel viewModel, String item) {
        super(viewModel);
        this.item.set(item);
    }
}
