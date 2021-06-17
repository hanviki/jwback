package com.welb.personnel_check.info;

/**
 * @author luoyaozu
 * @title: PersonnelScorringInfo
 * @projectName xh-360appraisal-interface
 * @description: 人事评分需要导入excel表的字段
 * @date 2019/8/1217:09
 */
public class PersonnelScorringInfo {

    private String moneycard;

    private String username;

    private String departmentname;

    private String score;

    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }
}
