package com.welb.organization_check.info;

/**
 * @author luoyaozu
 * @title: Info
 * @projectName xh-360appraisal-interface
 * @description: 评分汇总和历史评分汇总的模糊查询字段
 * @date 2019/6/1710:15
 */
public class Info {
    private String username;

    private String stationcode;

    private String year;

    private String month;

    private String state;

    private String scorestatus;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScorestatus() {
        return scorestatus;
    }

    public void setScorestatus(String scorestatus) {
        this.scorestatus = scorestatus;
    }
}
