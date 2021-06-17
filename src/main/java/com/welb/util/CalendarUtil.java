package com.welb.util;

import java.util.Calendar;

/**
 * @author luoyaozu
 * @title: CalendarUtil
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2923:12
 */
public class CalendarUtil {

    /**
     * 获取当前年份
     * @return
     */
    public static String getYear() {
        int y=Calendar.getInstance().get(Calendar.YEAR);
        String year = String.valueOf(y);
        return year;
    }

    /**
     * 获取当前月份
     * @return
     */
    public static String getMonth() {
        int m=Calendar.getInstance().get(Calendar.MONTH)+1;
        String month = String.valueOf(m);
        return month;
    }

    /**
     * 获取当前天数
     * @return
     */

    public static String getDay(){
        int d=Calendar.getInstance().get(Calendar.DATE);
        String day=String.valueOf(d);
        return day;
    }

    /**
     * 输入月份获取当前所在的季度
     * @param month
     * @return
     */
    public static String getQuarter(String month){
        int count = Integer.parseInt(month.trim());
        String state;
        if (count>0&&count<4){
           state="1";
        }
        else if (count<7){
            state="2";
        }else if (count<10){
            state="3";
        }else {
            state="4";
        }
        return state;
    }

}
