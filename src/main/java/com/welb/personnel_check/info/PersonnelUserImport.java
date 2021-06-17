package com.welb.personnel_check.info;

/**
 * @author luoyaozu
 * @title: PersonnelUserImport
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/3/2318:48
 */
public class PersonnelUserImport {


    private String moneycard;

    private String username;

    private String departmentname;


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

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }
}
