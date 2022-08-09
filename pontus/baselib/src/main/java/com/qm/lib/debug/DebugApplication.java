package com.qm.lib.debug;

import android.app.Application;

import com.dim.library.base.BaseApplication;
import com.qm.lib.config.ModuleLifecycleConfig;
import com.qm.lib.utils.RuntimeData;

/**
 * Created by dim on 2019/3/6
 * debug包下的代码不参与编译，仅作为独立模块运行时初始化数据
 */

public class DebugApplication extends BaseApplication {

    public static DebugApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);

        mApp = this;
        RuntimeData.getInstance().setHttpUrl("http://39.96.173.27/apppy/");
    }

    public Application getApplication() {
        return this;
    }
}
