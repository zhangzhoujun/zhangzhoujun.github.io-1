package com.qm.lib.base;

import android.os.Environment;

import java.io.File;

/**
 * @ClassName BaseAppViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 8:21 PM
 * @Version 1.0
 */
public class Constants {

    public static final String HIDE_LOADING_STATUS_MSG = "hide_loading_status_msg";

    // 一页个数
    public static final int PAGE_LIMIT = 20;
    public static final int COUNT_DOWN_NUM = 60;    // 获取短信倒计时


    public static final String PROJECT_NAME = "note";

    public static String CACHE_FILE_PATH = "";//缓存下载的apk

    public static final String FILE_CLIP_PATH = Environment.getExternalStorageDirectory() + File
            .separator + PROJECT_NAME + File.separator + "crop";
    public static final String CAMERA_IMAGE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "DCIM" + File.separator;

}
