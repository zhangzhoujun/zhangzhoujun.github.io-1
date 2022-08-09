package com.qm.module_common.ui.init;

import android.app.Application;

import com.dim.library.utils.DLog;
import com.qm.lib.base.IModuleInit;

public class CommonInit implements IModuleInit {

    @Override
    public boolean onInitAhead(Application application) {
        DLog.d("App 通用模块 -- onInitAhead");


        return false;
    }


    @Override
    public boolean onInitLow(Application application) {
        DLog.d("App 通用模块 -- onInitLow");
        return false;
    }
}
