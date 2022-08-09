package com.qm.lib.utils;

import com.dim.library.base.AppManager;
import com.dim.library.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName AppManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 10:56 AM
 * @Version 1.0
 */
public class JYAppManager {
    private static JYAppManager instance;

    private JYAppManager() {
    }

    /**
     * 单例模式
     *
     * @return AppManager
     */
    public static JYAppManager getAppManager() {
        if (instance == null) {
            instance = new JYAppManager();
        }
        return instance;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            MobclickAgent.onKillProcess(Utils.getContext());
            AppManager.getAppManager().AppExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
