package com.android.myvolley.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.myvolley.R;
import com.android.myvolley.model.MResponseBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseComFunc {

    /**
     * 判断两个时间是不是同一天
     * 
     * @param dateA
     * @param dateB
     * @return
     */
    public static boolean areSameDay(Date dateA, Date dateB) {
        if (dateA == null || dateB == null)
            return false;
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断两个时间是不是同一年
     *
     * @param dateA
     * @param dateB
     * @return
     */
    public static boolean areSameYear(Date dateA, Date dateB) {
        if (dateA == null || dateB == null)
            return false;
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR);
    }

    /**
     * 得到两个日期相差的天数
     */
    public static int getBetweenDay(Date date1, Date date2) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.parse(sdf.format(date1));
            date2 = sdf.parse(sdf.format(date2));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(date2);
            long time2 = cal.getTimeInMillis();
            between_days = (Math.abs(time2 - time1)) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 得到两个日期相差的小时
     */
    public static int getBetweenHour(Date start, Date end) {
        long cha = Math.abs(end.getTime() - start.getTime());
        double result = cha * 1.0 / (1000 * 60 * 60);
        return (int) result;
    }

    /**
     * 得到两个日期相差的天数(可以为负数date2减date1)
     */
    public static int getBetweenDayNotABS(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        System.out.println("days=" + days);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            // d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取距离当前时间通俗易懂的时间格式
     * 
     * @param date
     * @return
     */
    public static String getPopularDate(Date date, Context context) {
        if (date == null) {
            return "";
        }
        String strPoPular = "";
        Date nowDate = new Date();
        int diffDays = getBetweenDay(date, nowDate);
        if (areSameDay(nowDate, date)) {
            strPoPular = GetStringByDate(date, BaseComConst.TIME_FORMAT);// 今天context.getResources().getString(R.string.task_jintian)+
            // " "+
        } else if (diffDays == 1) {
            strPoPular = context.getResources().getString(R.string.message_yesterday);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 昨天
        } else if (diffDays == 2) {
            strPoPular = context.getResources().getString(R.string.message_befor_yesterday);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 前天
        } else if (diffDays > 2 && diffDays < 7) {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT);// DATE_WEEK_FORMAT
        } else {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT);
        }
        return strPoPular;
    }

    /**
     * 获取距离当前时间几天格式
     * 
     * @param date
     * @return
     */
    public static String getManyDate(Date date, Context context) {
        if (date == null) {
            return "";
        }

        String strPoPular = "";
        Date nowDate = new Date();
        int diffDays = getBetweenDayNotABS(date, nowDate);
        if (areSameDay(nowDate, date)) {
            strPoPular = context.getResources().getString(R.string.task_jintian);// 今天context.getResources().getString(R.string.task_jintian)+
            // " "+
        } else if (diffDays == 1) {
            strPoPular = context.getResources().getString(R.string.message_yesterday);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 昨天
        } else if (diffDays == 2) {
            strPoPular = context.getResources().getString(R.string.message_befor_yesterday);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 前天
        } else if (diffDays == -1) {
            strPoPular = context.getResources().getString(R.string.task_mingtian);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 明天
        } else if (diffDays == -2) {
            strPoPular = context.getResources().getString(R.string.task_houtian);
            // + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 后天
        } else if (diffDays > 2) {
            strPoPular = diffDays + context.getResources().getString(R.string.cal_day_ago);// DATE_WEEK_FORMAT
        } else if (diffDays < -2) {
            strPoPular = Math.abs(diffDays) + context.getResources().getString(R.string.cal_day_end);
        }
        return strPoPular;
    }

    public static String getPopularDate(Date date, Context context, Date nowDate) {
        String strPoPular = "";
        if (nowDate == null) {
            nowDate = new Date();
        }
        int diffDays = getBetweenDay(nowDate, date);
        if (areSameDay(nowDate, date)) {
            // strPoPular =
            // context.getResources().getString(R.string.task_jintian) + " "
            // + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 今天
            strPoPular = GetStringByDate(date, BaseComConst.TIME_FORMAT);// 今天
        } else if (diffDays == 1) {
            strPoPular = context.getResources().getString(R.string.message_yesterday) + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 昨天
        } else if (diffDays == 2) {
            strPoPular = context.getResources().getString(R.string.message_befor_yesterday) + " " + GetStringByDate(date, BaseComConst.TIME_FORMAT);// 前天
        } else if (diffDays > 2 && diffDays < 7) {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT_NOYEAR);
        } else {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT_NOYEAR);
        }
        return strPoPular;
    }

    /**
     * 获取时间显示 简单的
     * 
     * @param date
     * @param context
     * @param nowDate
     * @return
     */
    public static String getPopularDateSimple(Date date, Context context, Date nowDate) {
        String strPoPular = "";
        if (nowDate == null) {
            nowDate = new Date();
        }
        int diffDays = getBetweenDay(nowDate, date);
        if (areSameDay(nowDate, date)) {
            strPoPular = GetStringByDate(date, BaseComConst.TIME_FORMAT);// 今天
        } else if (diffDays == 1) {
            strPoPular = context.getResources().getString(R.string.message_yesterday);// 昨天
        } else if (diffDays == 2) {
            strPoPular = context.getResources().getString(R.string.message_befor_yesterday);// 前天
        } else if (diffDays > 2 && diffDays < 7) {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT_NOYEAR);
        } else {
            strPoPular = GetStringByDate(date, BaseComConst.DATE_FORMAT_NOYEAR);
        }
        return strPoPular;
    }

    /**
     * 判断是否为合法的日期时间字符串
     * 
     * @param str_input
     * @param str_input
     * @return boolean;符合为true,不符合为false
     */
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        if (str == null || "".equals(str) || "null".equals(str))
            return true;
        else
            return false;
    }

    /**
     * 拼装时间格式
     * 
     * @paramstrDate
     * @return
     */
    public static String GetStringByDate(Date date, String formatString) {
        String DateString = "";
        if (date != null)
            DateString = new SimpleDateFormat(formatString, Locale.getDefault()).format(date);
        return DateString;
    }

    /**
     * 拼装时间格式,如果不是同一年的,显示x年x月x日 x时x分 如果是同一年的,返回昨天前天今天后天明天格式
     * 
     * @param date
     * @param nowDate
     * @return
     */
    public static String GetStringByDate(Date date, Date nowDate, Context content) {
        if (date == null || nowDate == null) {
            return "";
        }
        if (!areSameYear(date, nowDate)) {
            return new SimpleDateFormat(BaseComConst.DATE_TIME_FORMAT, Locale.getDefault()).format(date);
        }
        return getPopularDateSimple(date, content, nowDate);
    }

    /**
     * 拼装时间格式,如果不是同一年的,显示x年x月x日 x时x分 如果是同一年的,返回不带年的
     *
     * @param date
     * @param nowDate
     * @return
     */
    public static String GetStringByDateEx(Date date, Date nowDate) {
        if (date == null || nowDate == null) {
            return "";
        }
        if (!areSameYear(date, nowDate)) {
            return new SimpleDateFormat(BaseComConst.DATE_TIME_FORMAT, Locale.getDefault()).format(date);
        }
        return new SimpleDateFormat(BaseComConst.DATE_TIME_NOYEAR_SECOND_FORMAT, Locale.getDefault()).format(date);
    }

    /**
     * 把毫秒转化成日期
     * 
     * @param dateFormat
     *            (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @parammillSec (秒数)
     * @return
     */
    public static String transferLongToDate(String dateFormat, Long Sec) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            Date date = new Date(Sec * 1000);
            return sdf.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }

    }

    /**
     * 拼装时间格式
     * 
     * @paramstrDate
     * @return
     */
    public static String GetStringByDate(Date date) {
        return GetStringByDate(date, BaseComConst.DATE_TIME_FORMAT);
    }

    /**
     * 根据字符串以及指定的类型获取对应的类型的时间字符串
     * 
     * @param dateString
     * @param thisType
     * @param needType
     * @return
     */
    public static String getStringByStringForDate(String dateString, String thisType, String needType) {
        Date date = GetDateByString(dateString, thisType);
        return GetStringByDate(date, needType);
    }

    /**
     * String→时间
     * 
     * @paramstrDate
     * @return
     */
    public static Date GetDateByString(String dateString, String formatString) {

        Date d = null;// new Date(1, 1, 1);// 默认值

        if (dateString != null && dateString.length() > 0)
            try {
                d = new SimpleDateFormat(formatString, Locale.getDefault()).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return d;
    }

    /**
     * String→时间
     * 
     * @param dateString
     *            时间的string值
     * @return
     */
    public static Date GetDateByString(String dateString) {
        return GetDateByString(dateString, BaseComConst.DATE_TIME_FORMAT);
    }

    /**
     * 拼装时间格式(用于发送WebServer,返回yyyy-MM-ddTHH:mm:ss格式)
     * 
     * @paramstrDate
     * @return
     */
    public static String GetWebServerAssembledDate(Date date) {
        String assembledDateString = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss", Locale.getDefault()).format(date).replace('@', 'T');
        return assembledDateString;// "2013-01-23T09:28:35"
    }

    /**
     * 拼装时间格式(用于发送WebServer,返回yyyy-MM-ddTHH:mm:ss格式)
     * 
     * @param strDate
     * @return
     */
    public static String GetWebServerAssembledDate(String strDate, String strFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat, Locale.getDefault());
        String assembledDateString = "";
        try {
            assembledDateString = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss", Locale.getDefault()).format(simpleDateFormat.parse(strDate)).replace('@', 'T');
        } catch (Exception e) {
            return "0001-01-01T00:00:00";
        }
        return assembledDateString;// "2013-01-23T09:28:35"
    }

    /**
     * 拆分时间格式(用来接收WebServer)
     * 
     * @param strDate
     *            传入参数默认时间格式为yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static Date SetWebServerSplitDate(String strDate) {
        String SplitDate = strDate.substring(0, strDate.length()).replace('T', ' ');// ZL修改
                                                                                    // strDate.substring(0,
                                                                                    // 19)会导致报错
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            date = sdf.parse(SplitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 判断服务器时间是够小于指定时间
     * 
     * @param serverTime
     * @param thisTime
     * @return
     */
    public static boolean doesServerTimeBig(String serverTime, String thisTime) {
        if (Long.parseLong(serverTime) - Long.parseLong(thisTime) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 拆分时间格式(用来接收WebServer) 注:时间格式必须和服务端时间格式相同
     * 
     * @param strDate
     * @param strFormat
     *            (服务端格式,目前服务器时间格式为yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static Date SetWebServerSplitDate(String strDate, String strFormat) {
        String SplitDate = strDate.substring(0, 19).replace('T', ' ');
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strFormat, Locale.getDefault());
            date = sdf.parse(SplitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 拆分时间格式(用来接收WebServer 将yyyy-MM-ddTHH:mm:ss转换为yyyy/MM/dd HH:mm:ss格式)
     * 
     * @param strDate
     * @return
     */
    public static String SetWebServerSplitDateToLocalString(String strDate) {
        if (strDate == null || "".equals(strDate) || "null".equals(strDate) || strDate.equals("0001-01-01T00:00:00")) {
            return "";
        } else {
            String SplitDate = strDate.substring(0, 19).replace('T', ' ');
            return SplitDate;
        }

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        // String telRegex = "[1][358]\\d{9}";//
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 前三后四中间星
     * 
     * @param mobile
     * @return
     */
    public static String getMobileShow(String mobile) {

        String returnMobile = "";
        if (mobile != null && mobile.length() >= 11) {
            returnMobile = mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return returnMobile;
    }

    /**
     * 获取字符串的长度，对双字符（包括汉字）按两位计数
     * 
     * @param value
     * @return
     */
    public static int getStrLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private static boolean matcher(String reg, String string) {
        boolean tem = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }

    /**
     * 简单提示对话框
     * 
     * @param Tips
     * @param context
     */
    public static void ShowTipDialog(String Tips, Context context) {
        try {
            final AlertDialog dialog = new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.system_tip))
                    .setIcon(android.R.drawable.ic_dialog_info).setMessage(Tips).setPositiveButton(context.getResources().getString(R.string.confirm), null)
                    .create();
            dialog.show();
        } catch (Exception e) {
            BaseToast.showShort(context, Tips);
            // TODO: handle exception
        }
    }

    public static void ShowTipDialog(String Tips, String des, Context context) {
        if (!BaseComFunc.isNull(des)) {
            Tips = Tips + des;
        }
        try {
            final AlertDialog dialog = new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.system_tip))
                    .setIcon(android.R.drawable.ic_dialog_info).setMessage(Tips).setPositiveButton(context.getResources().getString(R.string.confirm), null)
                    .create();
            dialog.show();
        } catch (Exception e) {
            BaseToast.showShort(context, Tips);
            // TODO: handle exception
        }
    }

    /**
     * 会自己消失的对话框
     * 
     * @param Tips
     * @param context
     */
    public static void ShowTipDialogDismiss(String Tips, Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.system_tip))
                .setIcon(android.R.drawable.ic_dialog_info).setMessage(Tips).setPositiveButton(context.getResources().getString(R.string.confirm), null)
                .create();
        dialog.show();
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }, 2000);
    }

    /**
     * 简单提示对话框
     * 
     * @paramTips
     * @param context
     */
    public static void ShowTipDialog(MResponseBase response, String tips, Context context) {
        if (response != null) {
            if (response.getBiz_msg() != null && !response.getBiz_msg().equals("") && !response.getBiz_msg().equals("null")) {
                tips = tips + ":" + response.getBiz_msg();
            }
        }
        ShowTipDialog(tips, context);
    }

    public static String getExceptionMessage(Throwable e) {
        if (e == null) {
            return "Exception对象为空..";
        }
        String separator = System.getProperty("line.separator");

        StringBuilder str = new StringBuilder("错误消息：");
        str.append("\t");
        str.append(e.getMessage());
        str.append(separator);

        str.append("堆栈信息：");
        for (StackTraceElement element : e.getStackTrace()) {
            str.append("\t");
            str.append(element.toString());
            str.append(separator);
        }

        return str.toString();
    }

    public static String readFromFile(File src) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    // 返回一个byte数组

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];
        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        return bytes;

    }

    // 这种序列号 缺点对于Android 2.2（“Froyo”）之前的设备不是100％的可靠(可以忽略)
    public static String getUUID(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

    }

    // dip转像素
    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);

        return valuePixels;

    }

    // 像素转dip
    public static float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float dips = Pixels / SCALE;

        return dips;

    }

    /**
     * 对网络连接状态进行判断
     * 
     * @return true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * 
     * @param context
     *            Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static String readAssets(Context context, String name) {
        String txt = "";
        try {
            InputStream is = context.getAssets().open(name);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            txt = new String(buffer);
        } catch (Exception e) {
        }
        return txt;
    }

    /**
     * 读取文件内容
     * 
     * @param fileName
     *            例如："message.txt"
     * @return
     */
    public static String read(String fileName, Context context) {
        try {
            String file = Environment.getExternalStorageDirectory().getPath() + fileName;
            File f = new File(file);
            // File f = context.getFileStreamPath(fileName);
            if (f.exists()) {
                return "";
            }
            FileInputStream inStream = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            inStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void write(String fileName, String msg, Context context) {
        // 步骤1：获取输入值
        if (msg == null)
            return;
        try {
            // 步骤2:创建一个FileOutputStream对象,
            // MODE_PRIVATE代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容
            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            // 步骤3：将获取过来的值放入文件
            fos.write(msg.getBytes());
            // 步骤4：关闭数据流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String CreateFileFold() {
        String folderPathString = Environment.getExternalStorageDirectory() + File.separator + "kalemao";
        File destDir = new File(folderPathString);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        // folderPathString += File.separator +
        // CommonUser.getInstance().getUser_id();
        destDir = new File(folderPathString);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return folderPathString;
    }

    public static String getLastName(String url) {
        String name = "";
        String[] ss = url.split("/");
        if (ss.length > 0) {
            name = ss[ss.length - 1];
        }
        return name;
    }

    public static boolean isSDCardCanRead() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 创建并返回shangchao/手机号/foldName文件夹
     *
     * @param foldName
     * @return
     */
    public static String CreateFileFoldDetile(String foldName) {
        String folderPathString = CreateFileFold();

        folderPathString += File.separator + foldName;
        File destDir = new File(folderPathString);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return folderPathString;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 根据URL判断是不是图片地址
     *
     * @param url
     * @return
     */
    public static boolean isPictureByUrl(String url) {
        if (url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".gif") || url.endsWith(".jepg") || url.endsWith(".bmp") || url.endsWith(".PNG")
                || url.endsWith(".JPG") || url.endsWith(".GIF") || url.endsWith(".JEPG") || url.endsWith(".BMP")) {
            return true;
        }
        return false;
    }

    // 检测GPRS是否打开S
    public static boolean gprsIsOpenMethod(Context context) {
        boolean gpsEnabled = Secure.isLocationProviderEnabled(context.getContentResolver(), LocationManager.GPS_PROVIDER);

        return gpsEnabled;
    }

    /**
     * 获取图片路径 android4.4及以上
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    // 判断该字符串是否为空
    public static boolean doesStringIsNull(String msg) {
        if (msg == null || "".equals(msg) || "null".equals(msg) || "nil".equals(msg)) {
            return true;
        }
        return false;
    }

    /**
     * 格式化销量
     *
     * @return
     */
    @SuppressLint("UseValueOf")
    public static String formatSellerNum(int num) {
        if (num >= 100000) {
            DecimalFormat df = new DecimalFormat("###.0");
            return df.format(new Double(Double.valueOf(num) / 10000)) + "万";
        } else {
            return getSaleNumFormat2(num);
        }
    }

    /**
     * 根据url获取商品详情的ID
     *
     * @param url
     * @return http://v.ewanse.com/goods/spu-detail?spuid=1108&token=10000
     */
    public static String getSpuidByUrl(String url) {
        String[] temp = null;
        temp = url.split("\\?");
        String[] temp1 = null;
        temp1 = temp[1].split("\\&");
        String[] temp2 = null;
        if (temp1[0].contains("spuid")) {
            temp2 = temp1[0].split("\\=");
        } else {
            temp2 = temp1[1].split("\\=");
        }
        return temp2[1];
    }

    /**
     * 根据url获取商品详情的搜索词
     *
     * @param url
     * @return // http://tt.ewanse.com/search/search-index?keywords=小熊
     */
    public static String getKeyByUrl(String url) {
        String[] temp = null;
        temp = url.split("\\=");
        if (temp.length > 1) {
            return temp[1];
        } else {
            return null;
        }
    }

    /**
     * 格式化获取2位小数
     *
     * @param f
     * @return
     */
    public static String get2DoubleOnly(String f) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            double d = Double.parseDouble(f);
            String price = df.format(d);
            if (price.indexOf(".") == 0) {
                price = "0" + price;
            }
            return price;

        } catch (Exception e) {
            return f;
        }
    }

    /**
     * 格式化获取2位小数
     *
     * @param f
     * @return
     */
    public static String get2Double(String f) {
        try {
            if (Double.valueOf(f) >= 100000) {
                DecimalFormat df = new DecimalFormat("###.00");
                return df.format(new Double(Double.valueOf(f) / 10000)) + "万";
            } else {
                DecimalFormat df = new DecimalFormat("#.00");
                double d = Double.parseDouble(f);
                String price = df.format(d);
                if (price.indexOf(".") == 0) {
                    price = "0" + price;
                }
                return price;
            }
        } catch (Exception e) {
            return f;
        }
    }

    /**
     * 格式化获取2位小数
     *
     * @param f
     * @return
     */
    public static String get1Double(String f) {
        try {
            DecimalFormat df = new DecimalFormat("#.0");
            double d = Double.parseDouble(f);
            String price = df.format(d);
            if (price.indexOf(".") == 0) {
                price = "0" + price;
            }
            return price;
        } catch (Exception e) {
            return f;
        }
    }

    /**
     * 格式化销量
     * 
     * @param num
     * @return
     */
    public static String getSaleNumFormat(long num) {
        if (num >= 100000) {
            DecimalFormat df = new DecimalFormat("###.0");
            return df.format(new Double(Double.valueOf(num) / 10000)) + "万";
        } else {
            return getSaleNumFormat2(num);
        }
    }

    public static String getSaleNumFormat2(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        } else if (num < 100000) {
            String str1 = String.valueOf(num);
            str1 = new StringBuilder(str1).reverse().toString(); // 先将字符串颠倒顺序
            String str2 = "";
            for (int i = 0; i < str1.length(); i++) {
                if (i * 3 + 3 > str1.length()) {
                    str2 += str1.substring(i * 3, str1.length());
                    break;
                }
                str2 += str1.substring(i * 3, i * 3 + 3) + ",";
            }
            if (str2.endsWith(",")) {
                str2 = str2.substring(0, str2.length() - 1);
            }
            // 最后再将顺序反转过来
            return new StringBuilder(str2).reverse().toString();

        } else {
            DecimalFormat df = new DecimalFormat("###.0");
            return df.format(new Double(Double.valueOf(num) / 10000)) + "万";
        }
    }

    public static void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int getGridViewItemHeight(GridView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return 0;
        }
        int index = 0;
        View listViewItem = listAdapter.getView(index, null, listView);
        // 计算子项View 的宽高
        listViewItem.measure(0, 0);
        // 计算所有子项的高度和
        totalHeight = listViewItem.getMeasuredHeight();

        return totalHeight;

    }

    /***
     * MD5加密 生成32位md5码
     * 
     * @param待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 储存当前店铺的token
     */
    public static void saveCurrentShopToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("shoptoken", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("shoptoken", token);
        editor.commit();
    }

    /**
     * 获取当前店铺的token
     * 
     */
    public static String getCurrentShopToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("shoptoken", Context.MODE_PRIVATE);
        return preferences.getString("shoptoken", "");
    }

    /**
     * 获取文字中的手机号，用，隔开
     * 
     * @param num
     * @return
     */
    public static String checkNum(String num) {
        if (num == null || num.length() == 0) {
            return "";
        }
        Pattern pattern = Pattern.compile("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}|\\d{4}-\\d{7}|1+[358]+\\d{9}|\\d{8}|\\d{7}");
        Matcher matcher = pattern.matcher(num);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    /**
     * 获取文字中的邮箱，用，隔开
     * 
     * @param num
     * @return
     */
    public static String checkEmail(String num) {
        if (num == null || num.length() == 0) {
            return "";
        }
        Pattern pattern = Pattern.compile("[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}");
        Matcher matcher = pattern.matcher(num);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    /**
     * 获取文字中的邮箱，用，隔开
     * 
     * @param num
     * @return
     */
    public static String checkWebSit(String num) {
        if (num == null || num.length() == 0) {
            return "";
        }
        Pattern pattern = Pattern
                .compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)");
        Matcher matcher = pattern.matcher(num);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    /**
     * 将字符串转换成Bitmap类型
     * 
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

}
