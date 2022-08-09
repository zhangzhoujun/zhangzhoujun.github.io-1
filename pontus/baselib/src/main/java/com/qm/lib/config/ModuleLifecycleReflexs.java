package com.qm.lib.config;

/**
 * Created by dim on 2019/3/6
 * 组件生命周期反射类名管理，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * 注意：以下模块中初始化的Module类不能被混淆
 */

public class ModuleLifecycleReflexs {

    private static final String BaseInit = "com.qm.lib.base.BaseModuleInit";
    private static final String UMInit = "com.qm.module_umshare.init.UMModuleInit";
    private static final String VersionInit = "com.qm.module_version.init.UpdateInit";
    private static final String CommonInit = "com.qm.module_common.ui.init.CommonInit";

    public static String[] initModuleNames = {BaseInit, UMInit, VersionInit, CommonInit};
}
