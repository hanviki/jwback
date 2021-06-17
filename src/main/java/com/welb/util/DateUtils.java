package com.welb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.welb.sysBase.util.Constant.*;

public class DateUtils {
    /**
     * 获取当前时间(format = yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String getNowTime(){
        return convertDateTimeToString(LocalDateTime.now());
    }

    /**
     * 根据指定年份,得出上一年
     */
    public static String getLastYear(String year){
        return String.valueOf(Integer.valueOf(year) - 1);
    }

    /**
     * 日期转字符串(format = yyyy-MM-dd HH:mm:ss)
     * @param dateTime
     * @return
     */
    public static String convertDateTimeToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        return dateTime.format(formatter);
    }

    /**
     * 字符串转日期(format = yyyy-MM-dd HH:mm:ss)
     * @param dateTime
     * @return
     */
    public static LocalDateTime convertStringToDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * 日期转字符串(format = yyyy-MM-dd)
     * @param date
     * @return
     */
    public static String convertDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        return date.format(formatter);
    }

    /**
     * 字符串转日期(format = yyyy-MM-dd)
     * @param date
     * @return
     */
    public static LocalDate convertStringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        return LocalDate.parse(date, formatter);
    }

    /**
     * 日期转字符串(format = HH:mm:ss)
     * @param time
     * @return
     */
    public static String convertTimeToString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
        return time.format(formatter);
    }

    /**
     * 字符串转日期(format = HH:mm:ss)
     * @param time
     * @return
     */
    public static LocalTime convertStringToTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
        return LocalTime.parse(time, formatter);
    }

    /**
     * 验证日期格式(yyyy-MM-dd)
     * @param dateStr
     * @return
     */
    public static boolean validateDate(String dateStr){
        Pattern pattern = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        Matcher matcher = pattern.matcher(dateStr);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    /**
     * 获取指定格式日期串
     */
    public static String getSpecificDateString(String format, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf.format(date);
    }

    public static List<String> getSpecifiedIntervalYears(String startYear, String endYear){
        List<String> resultList = new ArrayList<>();
        if (NumberUtil.isNumber(startYear) && NumberUtil.isNumber(endYear)){
            for (int i = Integer.valueOf(startYear); i <= Integer.valueOf(endYear); i ++){
                resultList.add(String.valueOf(i));
            }
        }

        return resultList;
    }
}
