package com.welb.personnel_check.entity;

public class DeptCheck {
    private String deptcheckcode;

    private String filename;

    private String filepath;

    private String year;

    private String moneycard;

    private String depart;
    //新增的字段
    private String username;

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
    }

    public String getDeptcheckcode() {
        return deptcheckcode;
    }

    public void setDeptcheckcode(String deptcheckcode) {
        this.deptcheckcode = deptcheckcode == null ? null : deptcheckcode.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
