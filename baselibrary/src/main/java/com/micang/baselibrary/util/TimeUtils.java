package com.micang.baselibrary.util;

import com.qiniu.android.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 * @version 1.0
 * @Package com.dizoo.baselibrary.util
 * @time 2019/4/8 10:29
 * @describe 时间相关
 */
public class TimeUtils {

    /**
     * 年-月-日 时:分:秒 显示格式
     */
    // 备注:如果使用大写HH标识使用24小时显示格式,如果使用小写hh就表示使用12小时制格式。
    public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-日 显示格式
     */
    public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";

    private static SimpleDateFormat simpleDateFormat;

    /**
     * 获取当前时间与给定时间的时间差(毫秒值)
     *
     * @param endTimeMillis
     * @return
     */
    public static long getDataMillis(long endTimeMillis) {
        //获取当前的毫秒值
        long currentTimeMillis = System.currentTimeMillis();
        long differTime = endTimeMillis * 1000 - currentTimeMillis;
        TLog.d("data", currentTimeMillis + "--------<" + differTime);
        return differTime;
    }

    /**
     * 获取当前时间与给定时间的 差值(天数)
     *
     * @param endTimeMillis
     * @return
     */
    public static String formatDuringDays(long endTimeMillis) {
        long dataMillis = getDataMillis(endTimeMillis);
        double days = dataMillis / (1000 * 60 * 60 * 24);
        int floor;
        if (days < 0) {
            floor = ((int) Math.floor(days - 0.5));
        } else {
            floor = ((int) Math.floor(days + 0.5));
        }
        return String.valueOf(floor);
    }

    /**
     * 获取当前时间与给定时间的 差值(小时)
     *
     * @param endTimeMillis
     * @return
     */
    public static String formatDuringHours(long endTimeMillis) {
        long dataMillis = getDataMillis(endTimeMillis);
        long hours = (dataMillis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        int floor = ((int) Math.floor(hours + 0.5));
        return String.valueOf(floor);
    }

    /**
     * 获取当前时间与给定时间的 差值(分钟)
     *
     * @param endTimeMillis
     * @return
     */
    public static String formatDuringMinutes(long endTimeMillis) {
        long dataMillis = getDataMillis(endTimeMillis);
        long minutes = (dataMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (dataMillis % (1000 * 60)) / 1000;
        double floor = Math.floor(minutes + 0.5);
        return String.valueOf(floor);
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringFullDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 字符串转毫秒
     *
     * @param time
     * @return
     */
    public static long formatDateMillis(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
        long times = 0;
        try {
            times = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return times;
    }
    /**
     * 日期字符串转毫秒
     *
     * @param time
     * @return
     */
    public static long formatShortDateMillis(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        long times = 0;
        try {
            times = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }
    /**
     * 毫秒转化字符串
     *
     * @param times
     * @return
     */
    public static String formatDate(long times) {
        Date currentTime = new Date(times);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 根据输入的内容返回时间的时间戳 空-0 日期-转换 时间戳-转换格式
     * @param time
     * @return
     */
    public  static  long getMillis(String time) {
        if (StringUtils.isBlank(time))
            return 0;
        if (time.contains("-"))
            return formatShortDateMillis(time) / 1000;
        return Long.parseLong(time);
    }
    /**
     * 获取时间格式hour:minute
     *
     * @param time
     * @return
     */
    public static String formattime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
        Date dateString = null;
        try {
            dateString = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String end = formatter1.format(dateString);
        return end;
    }
    /**
     * 获取时间格式hour:minute
     *
     * @param time
     * @return
     */
    public static String formattimem(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
        Date dateString = null;
        try {
            dateString = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String end = formatter1.format(dateString);
        return end;
    }
}
