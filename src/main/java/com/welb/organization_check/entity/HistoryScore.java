package com.welb.organization_check.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HistoryScore {
    public static HashMap<String,String> states=new LinkedHashMap<>();
    public static HashMap<String,String>scorestatusList=new LinkedHashMap<>();
    public static Map<String, String> roles = new LinkedHashMap<>();

    static {
        states.put("0","未提交");
        states.put("1","已提交");
        states.put("2","未评分");
        states.put("3","未完成评分");
        states.put("5","月结待提交");
        states.put("6","月结评分");
        states.put("7","月结评分完成");

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

    private String username;

    private String moneycard;

    private String departmentname;

    private String stationcode;

    private String rolecode;

    private String scorestatus;

    private String state;

    private String month;

    private String year;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;

    private Double totalscore;

    private String scorestatusname;

    private String statename;

    private String stationname;

    private String rolename;

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard == null ? null : moneycard.trim();
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname == null ? null : departmentname.trim();
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getScorestatus() {
        return scorestatus;
    }

    public void setScorestatus(String scorestatus) {
        this.scorestatus = scorestatus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
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

    public static HashMap<String, String> getStates() {
        return states;
    }

    public static void setStates(HashMap<String, String> states) {
        HistoryScore.states = states;
    }

    public static HashMap<String, String> getScorestatusList() {
        return scorestatusList;
    }

    public static void setScorestatusList(HashMap<String, String> scorestatusList) {
        HistoryScore.scorestatusList = scorestatusList;
    }

    public String getScorestatusname() {
        if (scorestatus!=null){
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

    public static Map<String, String> getRoles() {
        return roles;
    }

    public static void setRoles(Map<String, String> roles) {
        HistoryScore.roles = roles;
    }

    public String getRolename() {
        if(rolecode!=null){
            return  roles.get(rolecode);
        }
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
