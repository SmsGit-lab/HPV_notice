package com.sms.jiujia.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author songmingsong
 * @date 2023/5/5
 */
public class DateUtils {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final SimpleDateFormat FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 将 date 格式日期转换为 yyyy-MM-dd'T'HH:mm:ssX格式
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        return sdf.format(date);
    }

    /**
     * 获取当前的 yyyy-MM-dd'T'HH:mm:ssXXX 格式时间
     *
     * @return
     */
    public static String nowDate() {
        return sdf.format(new Date());
    }

    /**
     * 获取 yyyy-MM-dd'T'HH:mm:ssXXX 格式时间
     *
     * @return
     */
    public static String nowDate(Date date) {
        return sdf.format(date);
    }

    /**
     * 将 Date 格式日期转换为 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param dateTime
     * @return
     */
    public static String changeDate(Date dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(dateTime);
    }

    /**
     * date 转时间戳
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String DateTimeStamp(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return String.valueOf(sdf.parse(format).getTime() / 1000);
    }

    /**
     * 将 Date 格式日期转换为 yyyy年MM月dd日 HH:mm格式
     *
     * @param date
     * @return
     */
    public static String changeDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return simpleDateFormat.format(date);
    }    /**
     * 将 Date 格式日期转换为 yyyy年MM月dd日 HH:mm格式
     *
     * @param date
     * @return
     */
    public static Date changeDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(date);
    }

    /**
     * 将 Date 格式日期转换为 yyyyy-MM-dd HH:mm:ss格式
     *
     * @param date
     * @return
     */
    public static String changeDateTime3(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


    public static String changeDateDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        return simpleDateFormat.format(date);
    }

    public static String changeDateTime2(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }


    /**
     * 判断两个日期是否连续，单位天
     */
    public static boolean isContinuation(Date d1, Date d2) {
        try {
            Date parse1 = FORMAT_DAY.parse(FORMAT_DAY.format(d1));
            Date parse2 = FORMAT_DAY.parse(FORMAT_DAY.format(d2));
            return Math.abs(parse1.getTime() - parse2.getTime()) <= 24 * 60 * 60 * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加天数
     *
     * @param date
     * @param i
     * @return
     */
    public static Date addDate(Date date, int i) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加i天,整数  往后推,负数往前移动
        calendar.add(Calendar.DATE, i);
        //这个时间就是日期往后推i天的结果
        date = calendar.getTime();
        return date;
    }
}
