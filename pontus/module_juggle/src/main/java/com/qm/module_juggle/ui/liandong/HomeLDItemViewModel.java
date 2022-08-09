package com.qm.module_juggle.ui.liandong;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dim.library.base.ItemViewModel;
import com.dim.library.binding.command.BindingAction;
import com.dim.library.binding.command.BindingCommand;
import com.qm.module_juggle.entity.MHomeDataBean;
import com.qm.module_juggle.utils.JugglePathUtils;


/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020-01-08
 * 更改时间：2020-01-08
 * 版本号：1
 */
public class HomeLDItemViewModel extends ItemViewModel<HomeLDViewModel> {

    public ObservableField<MHomeDataBean.MHomeDataItemData> item = new ObservableField<>();
    public ObservableField<String> pos = new ObservableField<>();

    public HomeLDItemViewModel(@NonNull HomeLDViewModel viewModel, MHomeDataBean.MHomeDataItemData item, String pos) {
        super(viewModel);
        this.item.set(item);
        this.pos.set(pos);
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            JugglePathUtils.Companion.getInstance().onJuggleItemClick(item.get(), "LD_ITEM", -1, pos.get());
        }
    });
}
