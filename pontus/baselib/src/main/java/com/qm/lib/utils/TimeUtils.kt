package com.qm.lib.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


/**
 * @ClassName JYMMKVManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 3:48 PM
 * @Version 1.0
 */
class TimeUtils private constructor() {
    companion object {
        /**
         * 时间格式分隔符：-
         */
        const val DATE_SEPARATOR = "-"

        /**
         * 时间格式：yyyy-MM-dd
         */
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val DATE_FORMAT_POINT = "yyyy.MM.dd"

        /**
         * 时间格式：MM-dd
         */
        const val DATE_FORMAT_NOYEAR = "MM-dd"
        const val DATE_FORMAT_NOYEAR_POINT = "MM.dd"

        /**
         * 时间格式：yyyy-MM-dd HH:mm:ss
         */
        const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val DATE_TIME_FORMAT1 = "yyyy/MM/dd HH:mm:ss"

        /**
         * 时间格式：yyyy-MM-dd HH:mm
         */
        const val DATE_TIME_NOSECOND_FORMAT = "yyyy-MM-dd HH:mm"

        /**
         * 时间格式：yyyy.MM.dd HH:mm
         */
        const val DATE_TIME_NOSECOND_FORMAT_POINT = "yyyy.MM.dd HH:mm"

        /**
         * 时间格式：yyyy/MM
         */
        const val DATE_FORMAT_MOUNTH = "yyyy/MM"

        /**
         * 时间格式：yyyy-MM
         */
        const val DATE_FORMAT_YEAR_MOUNTH = "yyyy-MM"

        /**
         * 时间格式：yyyyMM
         */
        const val DATE_FORMAT_YEAR_MOUNTH_J = "yyyyMM"

        /**
         * 时间格式：yyyy
         */
        const val DATE_FORMAT_YEAR = "yyyy"

        /**
         * 时间格式：yyyy-MM
         */
        const val DATE_FORMAT_YEAR_MOUNTH_CHINESE = "yyyy年MM月"

        /**
         * 时间格式：MM-dd HH:mm
         */
        const val DATE_TIME_NOYEAR_SECOND_FORMAT = "MM-dd HH:mm"

        /**
         * 时间格式：MM-dd HH:mm:ss
         */
        const val DATE_TIME_NOYEAR_FORMAT = "MM-dd HH:mm:ss"

        /**
         * 时间格式：yyyy-MM-dd EEEE
         */
        const val DATE_WEEK_FORMAT = "yyyy-MM-dd EEEE"

        /**
         * 时间格式：MM-dd EEEE
         */
        const val DATE_WEEK_FORMAT_NO_YEAR = "MM-dd EEEE"

        /**
         * 时间格式：:
         */
        const val TIME_SEPARATOR = ":"

        /**
         * 时间格式：HH:mm
         */
        const val TIME_FORMAT = "HH:mm"

        /**
         * 时间格式：yyyy-MM-dd HH:00
         */
        const val DATE_HOUR_FORMAT = "yyyy-MM-dd HH:00"

        /**
         * 时间格式：EEEE
         */
        const val DATE_WEEK_FORMAT_ONLY = "EEEE"

        /**
         * 时间格式：yyyy年MM月dd日 HH:mm
         */
        const val DATE_TIME_NOSECOND_FORMAT_CHINA = "yyyy年MM月dd日 HH:mm:ss"

        /**
         * 时间格式：MM月dd日HH:mm
         */
        const val DATE_TIME_NOSECOND_NOYEAR_FORMAT_CHINA = "MM月dd日HH:mm"

        /**
         * 时间格式：yyyyMMddHHmmss
         */
        const val DATE_TIME_NOSECOND_FORMAT_FOR_NAME = "yyyyMMddHHmmss"

        val instance: TimeUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TimeUtils()
        }
    }

    /**
     * 把秒转化成日期
     *
     * @param dateFormat (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @parammillSec (秒数)
     */
    fun transferLongToDate(dateFormat: String?, Sec: Long): String {
        return try {
            val sdf =
                SimpleDateFormat(dateFormat, Locale.getDefault())
            val date = Date(Sec * 1000)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 拼装时间格式
     *
     * @paramstrDate
     */
    fun getStringByDate(date: Date?): String? {
        return GetStringByDate(date, DATE_TIME_FORMAT)
    }

    fun getTimeYMDHMS(time: Long): String? {
        var time = time
        if (time.toString().length < 13) {
            time = time * 1000
        }
        val data = Date(time)
        return GetStringByDate(data, DATE_TIME_FORMAT)
    }

    fun getTimeYMDHM(time: Long): String? {
        var time = time
        if (time.toString().length < 13) {
            time = time * 1000
        }
        val data = Date(time)
        return GetStringByDate(data, DATE_TIME_NOSECOND_FORMAT)
    }

    /**
     * 获取返回时间，年月日
     */
    fun getDateForYMD(timeString: String): String? {
        var timeString = timeString
        if (timeString.length < 13) {
            timeString += "000"
        }
        if (timeString.startsWith("-")) {
            return "/"
        }
        val format = SimpleDateFormat(DATE_FORMAT)
        val time = timeString.toLong()
        return format.format(time)
    }

    fun getDateForYMDPoint(timeString: String): String? {
        var timeString = timeString
        if (timeString.length < 13) {
            timeString += "000"
        }
        if (timeString.startsWith("-")) {
            return "/"
        }
        val format = SimpleDateFormat(DATE_FORMAT_POINT)
        val time = timeString.toLong()
        return format.format(time)
    }

    fun getDateForMDHMPoint(timeString: String): String? {
        var timeString = timeString
        if (timeString.length < 13) {
            timeString += "000"
        }
        if (timeString.startsWith("-")) {
            return "/"
        }
        val format = SimpleDateFormat(DATE_TIME_NOYEAR_SECOND_FORMAT)
        val time = timeString.toLong()
        return format.format(time)
    }

    fun getDateForMDPoint(timeString: String): String? {
        var timeString = timeString
        if (timeString.length < 13) {
            timeString += "000"
        }
        if (timeString.startsWith("-")) {
            return "/"
        }
        val format = SimpleDateFormat(DATE_FORMAT_NOYEAR_POINT)
        val time = timeString.toLong()
        return format.format(time)
    }

    /**
     * 获取返回时间，年月日
     */
    fun getDateForYMDHSM(time: Long): String? {
        var time = time
        if (time.toString().length < 13) {
            time *= 1000
        }
        val format =
            SimpleDateFormat(DATE_TIME_FORMAT)
        return format.format(time)
    }

    /**
     * 拼装时间格式
     *
     * @paramstrDate
     */
    fun GetStringByDate(date: Date?, formatString: String?): String? {
        var DateString: String? = ""
        if (date != null) {
            DateString =
                SimpleDateFormat(formatString, Locale.getDefault()).format(date)
        }
        return DateString
    }

    /**
     * 获取当天24点59分059秒
     */
    fun getTimesEndOfDay(): Long {
        val cal = Calendar.getInstance()
        cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH], 24, 59] = 59
        val endOfDate = cal.time
        endOfDate.time
        return endOfDate.time
    }

    /**
     * 判断两个时间戳是不是同一天
     */
    fun isSameDay(
        millis1: Long,
        millis2: Long,
        timeZone: TimeZone = TimeZone.getDefault()
    ): Boolean {
        val interval = abs(millis1 - millis2)
        return interval < 86400000 && interval > -86400000 && millis2Days(
            millis1,
            timeZone
        ) == millis2Days(millis2, timeZone)
    }

    private fun millis2Days(millis: Long, timeZone: TimeZone): Long {
        return (timeZone.getOffset(millis).toLong() + millis) / 86400000
    }

    /**
     * 获取两个时间相差的小时数
     */
    fun getTimeExpendHour(millis1: Long, millis2: Long): Long {
        val interval = abs(millis1 - millis2)
        return interval / (60 * 60 * 1000)
    }

    /**
     * 获取两个时间相差的天数
     */
    fun getTimeExpendDay(millis1: Long, millis2: Long): Long {
        val interval = abs(millis1 - millis2)
        return interval / (60 * 60 * 1000 * 24)
    }
}