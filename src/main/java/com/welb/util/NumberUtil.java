package com.welb.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class NumberUtil {
    /**
     * 判断一个字符串是否是数字(包括小数)
     */
    public static boolean isNumber(String value) {
        if (value == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(value).matches();
    }

    /**
     * 三位小数
     * @param value
     * @return
     */
    public static boolean isDoubleForThreeDecimalPlaces(String value) {
        if (value == null)
            return false;
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,3})?$");
        return pattern.matcher(value).matches();
    }

    /**
     * 整数验证
     */
    public static boolean isFigure(String value) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(value).matches();
    }

    /**
     * 身份证验证
     */
    public static boolean isIdCard(String idCard){
        //十八位
        Pattern eighteenCard = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
        //十五位
        Pattern fifteenCard = Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$");
        return eighteenCard.matcher(idCard).matches() || fifteenCard.matcher(idCard).matches();
    }

    /**
     * 计算百分百(保留五位小数)
     * @param denominator -分母
     * @param numberator - 分子
     * @return
     */
    public static Double calculatePercent(int numberator, int denominator){
        if (denominator == 0){
            return Double.valueOf(0);
        }

        Double sum = Double.valueOf(numberator)/Double.valueOf(denominator);
        BigDecimal decimal = BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }

    public static Double calculatePercentByDouble(double numberator, int denominator){
        if (denominator == 0){
            return Double.valueOf(0);
        }

        Double sum = Double.valueOf(numberator)/Double.valueOf(denominator);
        BigDecimal decimal = BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }

    public static Double calculatePerDoubleParams(double numberator, double denominator){
        if (denominator == 0){
            return Double.valueOf(0);
        }

        Double sum = Double.valueOf(numberator)/Double.valueOf(denominator);
        BigDecimal decimal = BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }
}
