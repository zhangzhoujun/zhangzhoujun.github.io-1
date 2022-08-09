package com.qm.lib.widget.refresh;

import androidx.databinding.BindingAdapter;

import com.dim.library.binding.command.BindingCommand;
import com.dim.library.utils.DLog;


/**
 * @author dim
 * @create at 2019/3/15 15:15
 * @description:
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand", "onRefreshStatusComand", "onLoadMoreStatusComand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(JYSwipeRefreshLayout layout, final BindingCommand onRefreshCommand, final BindingCommand onLoadMoreCommand,
                                                   final BindingCommand onRefreshStatusComand, final BindingCommand onLoadMoreStatusComand) {

        layout.setJYSwipeRefreshLayoutListener(new JYSwipeRefreshLayout.JYSwipeRefreshLayoutListener() {
            @Override
            public void onRefresh() {
                DLog.d("REFRESH", "onRefresh");
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }

            @Override
            public void onLoadMore() {
                DLog.d("REFRESH", "onLoadMore");
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
            }
        });

    }
}
