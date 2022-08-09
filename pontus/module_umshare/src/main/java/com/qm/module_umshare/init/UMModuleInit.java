package com.qm.module_umshare.init;

import android.app.Application;

import com.dim.library.utils.DLog;
import com.qm.lib.base.IModuleInit;
import com.umeng.socialize.PlatformConfig;

/**
 * @author dim
 * @create at 2019/4/1 15:42
 * @description:
 */
public class UMModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        DLog.e("友盟模块初始化 -- onInitAhead");

        PlatformConfig.setWeixin("wx6d32ec07bd563596","d0e445e54b7852c46b0c7c554148f16b");
        PlatformConfig.setQQZone("1110734484","sGy1q5vPECamVoZr");
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        DLog.e("友盟模块初始化 -- onInitLow");
        return false;
    }
}
