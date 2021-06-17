package com.welb.organization_check.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static Map<String, String> sexs = new LinkedHashMap<>();
    public static Map<String, String> politicals = new LinkedHashMap<>();
    public static Map<String, String> educations = new LinkedHashMap<>();
    public static Map<String, String> userstates = new LinkedHashMap<>();
    public static Map<String, String> roles = new LinkedHashMap<>();

    static {
        //性别
        sexs.put("1", "男");
        sexs.put("2", "女");
       //群众关系
        politicals.put("0", "中共党员");
        politicals.put("1,", "民主党派");
        politicals.put("2,", "无党派人士");
        politicals.put("3,", "群众");
        //学历
        educations.put("1", "博士");
        educations.put("2", "硕士");
        educations.put("3", "本科");
        educations.put("4", "专科");
        educations.put("5", "高中");

        //用户状态
        userstates.put("0","停用");
        userstates.put("1","启用");

        roles.put("50","组织部部长");
        roles.put("100","组织部");
        roles.put("150","打分用户");
        roles.put("200","部门长");
        roles.put("300","普通用户");
        roles.put("400","考勤超级管理员");
        roles.put("500","考勤管理员");

    }



    private String usercode;

    private String username;

    private String password;

    private String userstate;

    private String mobile;

    private String operator;

    private String stationcode;

    private String stationname;

    private String picturepath;

    private String sex;

    private String nation;

    private String education;

    private String email;

    private String political;

    private String moneycard;

    private String branchcode;

    private String branchname;

    private Double aratio=50.0;

    private Double bratio=20.0;

    private Double cratio=20.0;

    private Double dratio=10.0;

    private String fullstationcode;

    private String fullbranchcode;

    private String flag;

    private String roletype;

    private String isagent;
   //默认普通用户
    private String rolecode;

    private String rolename;
    //以下是新增的属性
    private String sexname;

    private String educationname;

    private String userstatename;

    private String politicalname;

    private String commoncode;

    private String commonname;

    private String departmentcode;

    private String departmentname;

    private List<Role>roleList=new ArrayList<>();

    private List<String> medicalEthicsRoleList = new ArrayList<>();

    //关联monthsummary表的员工编号
    private String employeecode;


    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserstate() {
        return userstate;
    }

    public void setUserstate(String userstate) {
        this.userstate = userstate == null ? null : userstate.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }


    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath == null ? null : picturepath.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political == null ? null : political.trim();
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard == null ? null : moneycard.trim();
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode == null ? null : branchcode.trim();
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

    public static Map<String, String> getRoles() {
        return roles;
    }

    public static void setRoles(Map<String, String> roles) {
        User.roles = roles;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getRolename() {
        if (rolecode!=null){
            return roles.get(this.rolecode);
        }
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    //新增的属性的get/set方法
    public static Map<String, String> getSexs() {
        return sexs;
    }

    public static void setSexs(Map<String, String> sexs) {
        User.sexs = sexs;
    }

    public static Map<String, String> getPoliticals() {
        return politicals;
    }

    public static void setPoliticals(Map<String, String> politicals) {
        User.politicals = politicals;
    }

    public static Map<String, String> getEducations() {
        return educations;
    }

    public static void setEducations(Map<String, String> educations) {
        User.educations = educations;
    }

    public static Map<String, String> getUserstates() {
        return userstates;
    }

    public static void setUserstates(Map<String, String> userstates) {
        User.userstates = userstates;
    }

    public String getSexname() {
        if (sex!=null){
            return sexs.get(this.sex);
        }
        return sexname;
    }

    public void setSexname(String sexname) {
        this.sexname = sexname;
    }

    public String getEducationname() {
        if(education!=null){
            return educations.get(this.education);
        }
        return educationname;
    }

    public void setEducationname(String educationname) {
        this.educationname = educationname;
    }

    public String getUserstatename() {
        if(userstate!=null){
            return userstates.get(this.userstate);
        }
        return userstatename;
    }

    public void setUserstatename(String userstatename) {
        this.userstatename = userstatename;
    }

    public String getPoliticalname() {
        if(political!=null){
            return politicals.get(this.political);
        }
        return politicalname;
    }

    public void setPoliticalname(String politicalname) {
        this.politicalname = politicalname;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getFullstationcode() {
        return fullstationcode;
    }

    public void setFullstationcode(String fullstationcode) {
        this.fullstationcode = fullstationcode;
    }

    public String getCommoncode() {
        return commoncode;
    }

    public void setCommoncode(String commoncode) {
        this.commoncode = commoncode;
    }

    public String getCommonname() {
        return commonname;
    }

    public void setCommonname(String commonname) {
        this.commonname = commonname;
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

    public String getFullbranchcode() {
        return fullbranchcode;
    }

    public void setFullbranchcode(String fullbranchcode) {
        this.fullbranchcode = fullbranchcode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }


    public String getIsagent() {
        return isagent;
    }

    public void setIsagent(String isagent) {
        this.isagent = isagent;
    }
}
