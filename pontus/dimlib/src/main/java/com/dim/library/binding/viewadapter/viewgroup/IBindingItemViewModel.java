package com.dim.library.binding.viewadapter.viewgroup;

/**
 * Created by  on 2017/6/15.
 */

import androidx.databinding.ViewDataBinding;

public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
