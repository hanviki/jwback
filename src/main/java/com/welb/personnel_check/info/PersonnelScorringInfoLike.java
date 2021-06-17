package com.welb.personnel_check.info;

/**
 * @author luoyaozu
 * @title: PersonnelSxorringInfoLike
 * @projectName xh-360appraisal-interface
 * @description: 人事评分导出excel表模糊查询的字段
 * @date 2019/8/1310:54
 */
public class PersonnelScorringInfoLike {

    private String year;

    private String month;

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
