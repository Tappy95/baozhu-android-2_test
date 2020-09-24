package com.micang.baselibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 * @version 1.0
 * @Package com.bigkoo.pickerviewdemo
 * @time 2019/3/1 23:52
 * @describe 把string转换为calendar
 */
public class StringUtil {

    public static Calendar getCalendar(String dateStr) {
        Calendar calendar = null;
        Date date = null;
        SimpleDateFormat format = null;//声明格式化日期的对象
        if (dateStr != null) {
            format = new SimpleDateFormat("yyyy-MM-dd");//创建日期的格式化类型
            calendar = Calendar.getInstance();
            try {
                date = format.parse(dateStr);
                calendar.setTime(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return calendar;
    }
}
