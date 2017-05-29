package com.example.androidlib.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *  将文章发表的时间转为相对于当前的时间
 * Created by liuyuhua on 2017/4/7.
 */

public class TransformTime {

    public static String getRelativeTimeString(String publish) {

        String result;

        // publish: 2017-04-07 20:02:50
        String[] time = publish.split("\\s", 2);

        // time[0]: 2017-04-07
        String[] yearMD = time[0].split("-", 3);
        int year = Integer.parseInt(yearMD[0]);
        int month = Integer.parseInt(yearMD[1]);
        int date = Integer.parseInt(yearMD[2]);

        // time[1]: 20:02:50
        String[] hourMS = time[1].split(":", 3);
        int hour = Integer.parseInt(hourMS[0]);
        int minute = Integer.parseInt(hourMS[1]);
        int second = Integer.parseInt(hourMS[2]);

        // 当前日期和时间
        GregorianCalendar now = new GregorianCalendar();
        int yearNow = now.get(Calendar.YEAR);
        int monthNow = now.get(Calendar.MONTH) + 1; // 返回值：0 - 11  代表：1月到12月
        int dateNow = now.get(Calendar.DATE);
        int hourNow = now.get(Calendar.HOUR_OF_DAY);
        int minuteNow = now.get(Calendar.MINUTE);

        if (year == yearNow && month == monthNow && date == dateNow) {
            if (hour == hourNow) {
                result = (minuteNow - minute) + "分钟前";
            } else {
                result = (hourNow - hour) + "小时前";
            }
        } else {
            // 不是同一天的，就以 2017-04-07 的格式返回
            result = time[0];
        }

        return result;
    }
}
