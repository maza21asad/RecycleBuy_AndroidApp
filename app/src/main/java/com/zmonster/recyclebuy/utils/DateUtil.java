package com.zmonster.recyclebuy.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String getDateTimeAgo24(Context context, long nDateTime) {
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 1);
        Date currentDate = cl.getTime();     //00:00 on the day
        Date lastDate = new Date(nDateTime); //目Standard time
        TimeSpan timeSpan = TimeSpan.subtract(currentDate, lastDate);
        int nDay = (int) timeSpan.getDays();
        boolean isChinese = SystemUtil.isChinese(context);
        Locale locale = isChinese ? Locale.CHINESE : Locale.ENGLISH;
        if (nDateTime >= currentDate.getTime()) {
            return getDateTimeByFormat(lastDate, "HH:mm", locale);
        } else if (nDay < 2) {
            return getDateTimeByFormat(lastDate, "HH:mm", locale) + (isChinese ? "yesterday" : "Yesterday");
        } else if (nDay > 7) {
            return getDateTimeByFormat(lastDate, "MMM d HH:mm", locale);
        }

        return getDateTimeByFormat(lastDate, "EEE HH:mm", locale);
    }

    public static String getDateTimeByFormat(Date d, String strFormat, Locale locale) {
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat, locale);//("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        return formatter.format(d);
    }

    public static String getDurationTime(int time) {
        return getDurationTime(time, ":", ":", "");
    }

    public static String getDurationTime(int time, String hStr, String mStr, String sStr) {
        int hour = time / 3600;
        int minute = time % 3600 / 60;
        int second = time % 60;

        String hour_fmt = String.format("%02d", hour);
        String minute_fmt = String.format("%02d", minute);
        String second_fmt = String.format("%02d", second);

        if (hour != 0)
            return hour_fmt + hStr + " " + minute_fmt + mStr + " " + second_fmt + sStr;
        else
            return minute_fmt + mStr + " " + second_fmt + sStr;

    }

}
