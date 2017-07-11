package com.kalemao.library.utils;

import android.os.Environment;

public class BaseComConst {
    public static final String WEB_SITE                               = "https://www.kalemao.com";

    public final static String STORE_PATH                             = Environment.getExternalStorageDirectory().getAbsolutePath() + "/klm/images/";

    public static final String DATE_SEPARATOR                         = "-";
    public static final String DATE_FORMAT                            = "yyyy-MM-dd";
    public static final String DATE_FORMAT_NOYEAR                     = "MM-dd";
    public static final String DATE_TIME_FORMAT                       = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_NOSECOND_FORMAT              = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_NOSECOND_FORMAT_POINT        = "yyyy.MM.dd HH:mm";
    public static final String DATE_TIME_NOYEAR_SECOND_FORMAT         = "MM-dd HH:mm";
    public static final String DATE_TIME_NOYEAR_FORMAT                = "MM-dd HH:mm:ss";
    public static final String DATE_WEEK_FORMAT                       = "yyyy-MM-dd EEEE";
    public static final String DATE_WEEK_FORMAT_NO_YEAR               = "MM-dd EEEE";
    public static final String TIME_SEPARATOR                         = ":";
    public static final String TIME_FORMAT                            = "HH:mm";
    public static final String DATE_HOUR_FORMAT                       = "yyyy-MM-dd HH:00";
    public static final String DATE_WEEK_FORMAT_ONLY                  = "EEEE";
    public static final String DATE_TIME_NOSECOND_FORMAT_CHINA        = "yyyy年MM月dd日 HH:mm";
    public static final String DATE_TIME_NOSECOND_NOYEAR_FORMAT_CHINA = "MM月dd日HH:mm";
    public static final String DATE_TIME_NOSECOND_FORMAT_FOR_NAME     = "yyyyMMddHHmmss";
    public static final String BACK_FOR_LOG_NAME                      = "BACK_FOR_LOG_NAME";

}
