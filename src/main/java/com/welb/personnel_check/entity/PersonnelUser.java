package com.welb.personnel_check.entity;

/**
 * @author luoyaozu
 * @title: PersonnelUserInfo
 * @projectName xh-360appraisal-interface
 * @description: 人事部用户需要导入excel表的字段
 * @date 2019/9/412:18
 */
public class PersonnelUser {
    private Integer id;

    private String moneycard;

    private String username;

    private String departmentname;

    private String starttime;

    private String endtime;

    private String flag;

    private String sex;

    private String birth;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "PersonnelUser{" +
                "id=" + id +
                ", moneycard='" + moneycard + '\'' +
                ", username='" + username + '\'' +
                ", departmentname='" + departmentname + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", flag='" + flag + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                '}';
    }
}
