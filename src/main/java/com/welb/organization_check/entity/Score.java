package com.welb.organization_check.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Score {
    public static Map<String, String> scoretypes = new LinkedHashMap<>();

    static {
        scoretypes.put("A", "A类评分");
        scoretypes.put("B", "B类评分");
        scoretypes.put("C", "C类评分");
        scoretypes.put("D", "D类评分");
    }

    private String id;

    private String scorringcode;

    private String scorringname;

    private String scorredcode;

    private String scorredname;

    private String scoretype;

    private String scoretypename;

    //评分人岗位code
    private String stationcode1;
    //被评分人岗位code
    private String stationcode2;
    //评分人岗位名称
    private String stationname1;
    //被评分人岗位名称
    private String stationname2;
    //被评分人部门code
    private String departmentcode1;
    //被评分人部门code
    private String departmentcode2;
    //被评分人部门名称
    private String departmentname1;
    //被评分人部门名称
    private String departmentname2;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode == null ? null : scorringcode.trim();
    }

    public String getScorredcode() {
        return scorredcode;
    }

    public void setScorredcode(String scorredcode) {
        this.scorredcode = scorredcode == null ? null : scorredcode.trim();
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }

    public static Map<String, String> getScoretypes() {
        return scoretypes;
    }

    public static void setScoretypes(Map<String, String> scoretypes) {
        Score.scoretypes = scoretypes;
    }

    public String getScorringname() {
        return scorringname;
    }

    public void setScorringname(String scorringname) {
        this.scorringname = scorringname;
    }

    public String getScorredname() {
        return scorredname;
    }

    public void setScorredname(String scorredname) {
        this.scorredname = scorredname;
    }

    public String getScoretypename() {
        if (scoretype != null) {
            return scoretypes.get(this.scoretype);
        }
        return scoretypename;
    }

    public void setScoretypename(String scoretypename) {
        this.scoretypename = scoretypename;
    }

    public String getStationcode1() {
        return stationcode1;
    }

    public void setStationcode1(String stationcode1) {
        this.stationcode1 = stationcode1;
    }

    public String getStationcode2() {
        return stationcode2;
    }

    public void setStationcode2(String stationcode2) {
        this.stationcode2 = stationcode2;
    }

    public String getStationname1() {
        return stationname1;
    }

    public void setStationname1(String stationname1) {
        this.stationname1 = stationname1;
    }

    public String getStationname2() {
        return stationname2;
    }

    public void setStationname2(String stationname2) {
        this.stationname2 = stationname2;
    }

    public String getDepartmentcode1() {
        return departmentcode1;
    }

    public void setDepartmentcode1(String departmentcode1) {
        this.departmentcode1 = departmentcode1;
    }

    public String getDepartmentcode2() {
        return departmentcode2;
    }

    public void setDepartmentcode2(String departmentcode2) {
        this.departmentcode2 = departmentcode2;
    }

    public String getDepartmentname1() {
        return departmentname1;
    }

    public void setDepartmentname1(String departmentname1) {
        this.departmentname1 = departmentname1;
    }

    public String getDepartmentname2() {
        return departmentname2;
    }

    public void setDepartmentname2(String departmentname2) {
        this.departmentname2 = departmentname2;
    }
}
