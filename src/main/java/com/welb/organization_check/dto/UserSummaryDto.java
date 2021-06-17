package com.welb.organization_check.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author luoyaozu
 * @title: UserSummaryDto
 * @projectName xh-360appraisal-interface
 * @description: 个人评分dto
 * @date 2019/6/510:51
 */
public class UserSummaryDto {
    //个人季度总结属性
    public static HashMap<String, String> states = new LinkedHashMap<>();
    public static HashMap<String, String> months = new LinkedHashMap<>();

    static {
        states.put("", "--");
        states.put("0", "未提交");
        states.put("1", "已提交");
        states.put("5", "季结待提交");
        states.put("6", "季结评分");
        states.put("7", "季结评分完成");

        months.put("1", "第一季度");
        months.put("2", "第二季度");
        months.put("3", "第三季度");
        months.put("4", "第四季度");
    }

    private String employeecode;

    private String year;

    private String month;

    private String monthname;

    private String state;

    private String statename;

    private String serialno;

    private String title;

    private String savepath;

    private String filename;

    private String content;

    //用户的属性
    private String usercode;//用户主键

    private String username;

    private String stationcode;

    private String stationname;

    private String fullstationcode;

    private String moneycard;

    private String departmentcode;

    private String departmentname;


    private String scorringcode;

    private String scorringname;

    private String scorredcode;

    private String scorredname;
    //打分状态 如果打了分就显示分值  没有 默认字段为评分
    private String status;

    private double total;//总分

    private String isedit;//是否可编辑  0：可编辑   1：不可编辑



    public static HashMap<String, String> getStates() {
        return states;
    }

    public static void setStates(HashMap<String, String> states) {
        UserSummaryDto.states = states;
    }

    public static HashMap<String, String> getMonths() {
        return months;
    }

    public static void setMonths(HashMap<String, String> months) {
        UserSummaryDto.months = months;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthname() {
        if (month!=null){
            return months.get(this.month);
        }
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatename() {
        if (state!=null){
            return states.get(this.state);
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getFullstationcode() {
        return fullstationcode;
    }

    public void setFullstationcode(String fullstationcode) {
        this.fullstationcode = fullstationcode;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode;
    }

    public String getScorringname() {
        return scorringname;
    }

    public void setScorringname(String scorringname) {
        this.scorringname = scorringname;
    }

    public String getScorredcode() {
        return scorredcode;
    }

    public void setScorredcode(String scorredcode) {
        this.scorredcode = scorredcode;
    }

    public String getScorredname() {
        return scorredname;
    }

    public void setScorredname(String scorredname) {
        this.scorredname = scorredname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getIsedit() {
        return isedit;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

}
