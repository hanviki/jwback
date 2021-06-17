package com.welb.personnel_check.info;

/**
 * @author luoyaozu
 * @title: PesonnelUserInfo
 * @projectName xh-360appraisal-interface
 * @description: 人员信息导出excel表模糊查询字段
 * @date 2020/3/1611:23
 */
public class PersonnelUserInfo {
    private String username;

    private String departmentname;

    private String flag;



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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
