package com.welb.personnel_check.info;

/**
 * @author luoyaozu
 * @title: PersonnelInfo
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/3/1614:56
 */
public class PersonnelInfo {
    private String year;

    private String month;

    private String username;

    private String departmentname;

    private String score1;

    private String score2;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }
}
