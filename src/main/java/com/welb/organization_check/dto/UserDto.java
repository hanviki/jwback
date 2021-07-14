package com.welb.organization_check.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luoyaozu
 * @title: UserDto
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/414:56
 */
public class UserDto {
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

    private String year;

    private String month;

    private String monthname;

    private String state;

    private String title;

    private String content;

    private String statename;

    private String serialno;

    private String employeecode;

    private String employeename;

    private Date pubdate;

    private String savepath;

    private String filename;

    private String issend;

    private String roletype;

    //用户的属性
    private String usercode;//用户主键

    private String username;

    private String stationcode;

    private String stationname;

    private String fullstationcode;

    private String moneycard;

    private String departmentcode;

    private String departmentname;

    private String flag;

    private Double aratio;

    private Double bratio;

    private Double cratio;

    private Double dratio;

    private Double AScore;

    private Double BScore;

    private Double CScore;

    private Double DScore;

    private Double TotalScore;

    private String mobile;

    private String scorestatus="";

    private String scorestatusname;

    private String rolecode;

    private String rolename;

    public String getScorestatus() {
        return scorestatus;
    }

    public void setScorestatus(String scorestatus) {
        this.scorestatus = scorestatus;
    }

    public static HashMap<String, String> getScoreStatusList() {
        return scorestatusList;
    }

    public static void setScoreStatusList(HashMap<String, String> scoreStatusList) {
        scorestatusList = scorestatusList;
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

    public String getStatename() {
        if (state != null) {
            return states.get(this.state);
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

  /*  public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }*/

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

    public Double getAratio() {
        return aratio;
    }

    public void setAratio(Double aratio) {
        this.aratio = aratio;
    }

    public Double getBratio() {
        return bratio;
    }

    public void setBratio(Double bratio) {
        this.bratio = bratio;
    }

    public Double getCratio() {
        return cratio;
    }

    public void setCratio(Double cratio) {
        this.cratio = cratio;
    }

    public Double getDratio() {
        return dratio;
    }

    public void setDratio(Double dratio) {
        this.dratio = dratio;
    }

    public Double getAScore() {
        return AScore;
    }

    public void setAScore(Double AScore) {
        this.AScore = AScore;
    }

    public Double getBScore() {
        return BScore;
    }

    public void setBScore(Double BScore) {
        this.BScore = BScore;
    }

    public Double getCScore() {
        return CScore;
    }

    public void setCScore(Double CScore) {
        this.CScore = CScore;
    }

    public Double getDScore() {
        return DScore;
    }

    public void setDScore(Double DScore) {
        this.DScore = DScore;
    }

    public Double getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(Double totalScore) {
        TotalScore = totalScore;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
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

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public static HashMap<String, String> getScorestatusList() {
        return scorestatusList;
    }

    public static void setScorestatusList(HashMap<String, String> scorestatusList) {
        UserDto.scorestatusList = scorestatusList;
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
