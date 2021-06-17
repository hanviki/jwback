package com.welb.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class ExportExcelUtils {

    /**
     * 验证日期格式(yyyy-MM-dd)
     */
    public static boolean checkDateFormat(String time, int row, int col, String colName, List<String> resultList) {
        if (!StringUtils.isEmpty(time) && !DateUtils.validateDate(time)) {
            resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]格式不正确(正确格式如:2011/1/1),请修改后上传!");
            return true;
        }

        return false;
    }

    /**
     * 数字及长度验证
     */
    public static boolean checkItemIsNumAndLength(String item, int row, int col, String colName, boolean checkLength, int length, List<String> resultList) {
        if (checkLength) {
            if (!StringUtils.isEmpty(item) && !NumberUtil.isNumber(item)) {
                //数字
                resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]不是数字,请修改后上传!");
                return false;
            }

            if (!StringUtils.isEmpty(item) && NumberUtil.isNumber(item) && item.length() != length) {
                //是数字,但长度不符合
                resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]数字长度不等于" + length + ",请修改后上传!");
                return false;
            }
            return true;
        } else {
            if (!StringUtils.isEmpty(item) && !NumberUtil.isNumber(item)) {
                //非数字
                resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]不是数字,请修改后上传!");
                return false;
            }

            return true;
        }
    }

    /**
     * 验证字段是否为空
     */
    public static boolean checkItemIsEmpty(String item, int row, int col, String colName, List<String> resultList) {
        if (StringUtils.isEmpty(item)) {
            resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]为空,请修改后上传!");
            return true;
        }

        return false;
    }

    public static void isDoubleForThreeDecimalPlaces(String item, int row, int col, String colName, List<String> resultList) {
        if (!NumberUtil.isDoubleForThreeDecimalPlaces(item)) {
            resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]不正确(格式：纯数字,最高保留三位小数),请修改后上传!");
        }
    }

    public static void isNumberString(String item, int row, int col, String colName, List<String> resultList) {
        if (!NumberUtil.isNumber(item.replace(",", ""))) {
            resultList.add("第" + row + "行" + col + "列" + "[" + colName + "]不正确(格式：纯数字,数字以逗号分割),请修改后上传!");
        }
    }

    public static String removeSpace(String item){
        if (StringUtils.isNotEmpty(item)){
            return item.trim();
        }else {
            return item;
        }
    }
}
