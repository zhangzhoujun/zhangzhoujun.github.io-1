package com.qm.module_version.init;

import android.app.Application;

import com.dim.library.utils.DLog;
import com.qm.lib.base.IModuleInit;

import zlc.season.rxdownload3.core.DownloadConfig;
import zlc.season.rxdownload3.extension.ApkInstallExtension;
import zlc.season.rxdownload3.extension.ApkOpenExtension;

public class UpdateInit implements IModuleInit {

    @Override
    public boolean onInitAhead(Application application) {
        DLog.d("App 版本更新模块 -- onInitAhead");

        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(application)
                .enableDb(true)
                .setDebug(true)
                .enableNotification(true)
                .addExtension(ApkInstallExtension.class)
                .addExtension(ApkOpenExtension.class);


        DownloadConfig.INSTANCE.init(builder);
        return false;
    }



    @Override
    public boolean onInitLow(Application application) {
        DLog.d("App 版本更新模块 -- onInitLow");
        return false;
    }
}
