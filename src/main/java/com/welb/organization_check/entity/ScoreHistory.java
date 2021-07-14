package com.welb.organization_check.entity;

import com.welb.organization_check.dto.UserDto;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreHistory {
    //个人季度总结属性
    public static HashMap<String, String> states = new LinkedHashMap<>();
    public static HashMap<String, String> months = new LinkedHashMap<>();
    public static HashMap<String, String> scorestatusList = new LinkedHashMap<>();
    public static Map<String, String> roles = new LinkedHashMap<>();


    static {
        states.put("", "--");
        states.put("0", "未提交");
        states.put("1", "已提交");
        states.put("5", "待提交");
        states.put("6", "评分中");
        states.put("7", "评分完成");

        months.put("1", "1月");
        months.put("2", "2月");
        months.put("3", "3月");
        months.put("4", "4月");
        months.put("5", "5月");
        months.put("6", "6月");
        months.put("7", "7月");
        months.put("8", "8月");
        months.put("9", "9月");
        months.put("10", "10月");
        months.put("11", "11月");
        months.put("12", "12月");

        scorestatusList.put("1","未评分");
        scorestatusList.put("2","未完成");
        scorestatusList.put("3","已完成");


        roles.put("50","组织部部长");
        roles.put("100","组织部");
        roles.put("150","打分用户");
        roles.put("200","部门长");
        roles.put("300","普通用户");
        roles.put("400","考勤超级管理员");
        roles.put("500","考勤管理员");


    }



    private Integer id;

    private String usercode;

    private String scorestatus;

    public static HashMap<String, String> getScorestatusList() {
        return scorestatusList;
    }

    public static void setScorestatusList(HashMap<String, String> scorestatusList) {
        ScoreHistory.scorestatusList = scorestatusList;
    }

    public String getScorestatus() {
        return scorestatus;
    }

    public void setScorestatus(String scorestatus) {
        this.scorestatus = scorestatus;
    }

    public String getScorestatusname() {
        if(scorestatus!=null){
            return scorestatusList.get(scorestatus);
        }
        return scorestatusname;
    }

    public void setScorestatusname(String scorestatusname) {
        this.scorestatusname = scorestatusname;
    }

    public String getStatename() {
        if (state!=null){
            return states.get(state);
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    private String scorestatusname;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;

    private Double totalscore;

    private Double sumZdScore;

    private Double sumMbScore;

    private Double avgZdScore;

    private Double avgMbScore;

    //新增联合属性


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }


    public Double getAscore() {
        return ascore;
    }

    public void setAscore(Double ascore) {
        this.ascore = ascore;
    }

    public Double getBscore() {
        return bscore;
    }

    public void setBscore(Double bscore) {
        this.bscore = bscore;
    }

    public Double getCscore() {
        return cscore;
    }

    public void setCscore(Double cscore) {
        this.cscore = cscore;
    }

    public Double getDscore() {
        return dscore;
    }

    public void setDscore(Double dscore) {
        this.dscore = dscore;
    }

    public Double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(Double totalscore) {
        this.totalscore = totalscore;
    }

    public Double getSumZdScore() { return sumZdScore; }

    public void setSumZdScore(Double sumZdScore) {
        this.sumZdScore = sumZdScore;
    }

    public Double getSumMbScore() { return sumMbScore; }

    public void setSumMbScore(Double sumMbScore) {
        this.sumMbScore = sumMbScore;
    }

    public Double getAvgZdScore() { return avgZdScore; }

    public void setAvgZdScore(Double avgZdScore) {
        this.avgZdScore = avgZdScore;
    }

    public Double getAvgMbScore() { return avgMbScore; }

    public void setAvgMbScore(Double avgMbScore) {
        this.avgMbScore = avgMbScore;
    }

    private String year;

    private String month;

    private String monthname;

    private String state;

    private String statename;

    private String serialno;

    private String employeecode;

    private String employeename;

    private String username;

    private String stationcode;

    private String stationname;

    private String moneycard;

    private String departmentcode;

    private String departmentname;

    private String flag;

    private String rolecode;

    private String rolename;

    private String title;

    private String content;

    private String savepath;

    private String filename;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public static HashMap<String, String> getStates() {
        return states;
    }

    public static void setStates(HashMap<String, String> states) {
        UserDto.states = states;
    }

    public static HashMap<String, String> getMonths() {
        return months;
    }

    public static void setMonths(HashMap<String, String> months) {
        UserDto.months = months;
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
        if (month != null) {
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }


    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public static Map<String, String> getRoles() {
        return roles;
    }

    public static void setRoles(Map<String, String> roles) {
        UserDto.roles = roles;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getRolename() {
        if(rolecode!=null){
            return this.roles.get(rolecode);
        }
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
