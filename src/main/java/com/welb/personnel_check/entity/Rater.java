package com.welb.personnel_check.entity;

public class Rater {

    private String ratercode;

    private String scorringcode;

    private String scorringname;

    private String phone;

    private String department;

    private String leaderphone;

    private String remarks;

    private String remarks2;

    private String flag;

    private String usercode;

    public String getRatercode() {
        return ratercode;
    }

    public void setRatercode(String ratercode) {
        this.ratercode = ratercode;
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode == null ? null : scorringcode.trim();
    }

    public String getScorringname() {
        return scorringname;
    }

    public void setScorringname(String scorringname) {
        this.scorringname = scorringname == null ? null : scorringname.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getLeaderphone() {
        return leaderphone;
    }

    public void setLeaderphone(String leaderphone) {
        this.leaderphone = leaderphone;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
}
