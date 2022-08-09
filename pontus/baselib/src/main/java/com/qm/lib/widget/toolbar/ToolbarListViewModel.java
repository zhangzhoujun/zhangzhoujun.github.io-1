package com.qm.lib.widget.toolbar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dim.library.binding.command.BindingAction;
import com.dim.library.binding.command.BindingCommand;
import com.dim.library.utils.DLog;
import com.qm.lib.base.ListViewModel;

/**
 * @author dim
 * @create at 2019/3/13 13:31
 * @description: 带Toolbar的 基础VM
 */
public abstract class ToolbarListViewModel extends ListViewModel {
    /**
     * JYToolbar的配置
     */
    public ObservableField<JYToolbarOptions> toolbarOptions = new ObservableField<>();

    public ToolbarListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 设置JYToolbar的JYToolbarOptions
     *
     * @param options
     */
    public void setOptions(JYToolbarOptions options) {
        toolbarOptions.set(options);
    }

    /**
     * 设置JYToolbar的返回点击
     *
     * @param options
     */
    public BindingCommand onBackClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            setOnBackClick();
        }
    });

    /**
     * 设置JYToolbar的右边按钮的点击
     *
     * @param options
     */
    public BindingCommand onRightClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onRightClick();
        }
    });

    /**
     * 设置JYToolbar的右边第二个按钮的点击
     *
     * @param options
     */
    public BindingCommand onRightLeftClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onRightLeftClick();
        }
    });

    /**
     * 设置JYToolbar的关闭按钮的点击
     */
    public BindingCommand onCloseClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onViewCloseClick();
        }
    });

    /**
     * 子类重写，右边按钮的点击事件
     */
    protected void setOnBackClick() {
        DLog.d("JYToolbar --> onBackClick");
        onBackPressed();
    }

    /**
     * 子类重写，右边按钮的点击事件
     */
    protected void onRightClick() {
        DLog.d("JYToolbar --> onRightClick");
    }

    /**
     * 子类重写，右边第二个按钮的点击事件
     */
    protected void onRightLeftClick() {
        DLog.d("JYToolbar --> onRightLeftClick");
    }

    /**
     * 子类重写，关闭按钮的点击事件
     */
    protected void onViewCloseClick() {
        DLog.d("JYToolbar --> onViewCloseClick");
    }
}
