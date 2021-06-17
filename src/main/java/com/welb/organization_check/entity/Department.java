package com.welb.organization_check.entity;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String departmentcode;

    private String departmentname;

    private String updepartmentcode;

    private String departmentdesc;

    private List<Department> childDept =new ArrayList();

    private List<Station> stations =new ArrayList();

    private String commoncode;

    private String commonname;


    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode == null ? null : departmentcode.trim();
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname == null ? null : departmentname.trim();
    }

    public String getUpdepartmentcode() {
        return updepartmentcode;
    }

    public void setUpdepartmentcode(String updepartmentcode) {
        this.updepartmentcode = updepartmentcode == null ? null : updepartmentcode.trim();
    }

    public String getDepartmentdesc() {
        return departmentdesc;
    }

    public void setDepartmentdesc(String departmentdesc) {
        this.departmentdesc = departmentdesc == null ? null : departmentdesc.trim();
    }

    public List<Department> getChildDept() {
        return childDept;
    }

    public void setChildDept(List<Department> childDept) {
        this.childDept = childDept;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
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
}
