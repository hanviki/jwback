package com.welb.organization_check.entity;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String stationcode;

    private String stationname;

    private String departmentcode;

    private String departmentname;

    private String stationdesc;

    private String dutydesc;

    private String relation1;

    private String relation2;

    private String station1;

    private String station2;

    private String xueli;

    private String zhiyezige;

    private String peixun;

    private String gongzuojingyan;

    private String gexingtezheng;

    private String jibenjineng;

    private Integer orderid;

    private String fulldepartmentcode;


    private List<User> userTree = new ArrayList<>();

    private String commoncode;

    private String commonname;


    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode == null ? null : stationcode.trim();
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname == null ? null : stationname.trim();
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode == null ? null : departmentcode.trim();
    }

    public String getStationdesc() {
        return stationdesc;
    }

    public void setStationdesc(String stationdesc) {
        this.stationdesc = stationdesc == null ? null : stationdesc.trim();
    }

    public String getDutydesc() {
        return dutydesc;
    }

    public void setDutydesc(String dutydesc) {
        this.dutydesc = dutydesc == null ? null : dutydesc.trim();
    }

    public String getRelation1() {
        return relation1;
    }

    public void setRelation1(String relation1) {
        this.relation1 = relation1 == null ? null : relation1.trim();
    }

    public String getRelation2() {
        return relation2;
    }

    public void setRelation2(String relation2) {
        this.relation2 = relation2 == null ? null : relation2.trim();
    }

    public String getStation1() {
        return station1;
    }

    public void setStation1(String station1) {
        this.station1 = station1 == null ? null : station1.trim();
    }

    public String getStation2() {
        return station2;
    }

    public void setStation2(String station2) {
        this.station2 = station2 == null ? null : station2.trim();
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli == null ? null : xueli.trim();
    }

    public String getZhiyezige() {
        return zhiyezige;
    }

    public void setZhiyezige(String zhiyezige) {
        this.zhiyezige = zhiyezige == null ? null : zhiyezige.trim();
    }

    public String getPeixun() {
        return peixun;
    }

    public void setPeixun(String peixun) {
        this.peixun = peixun == null ? null : peixun.trim();
    }

    public String getGongzuojingyan() {
        return gongzuojingyan;
    }

    public void setGongzuojingyan(String gongzuojingyan) {
        this.gongzuojingyan = gongzuojingyan == null ? null : gongzuojingyan.trim();
    }

    public String getGexingtezheng() {
        return gexingtezheng;
    }

    public void setGexingtezheng(String gexingtezheng) {
        this.gexingtezheng = gexingtezheng == null ? null : gexingtezheng.trim();
    }

    public String getJibenjineng() {
        return jibenjineng;
    }

    public void setJibenjineng(String jibenjineng) {
        this.jibenjineng = jibenjineng == null ? null : jibenjineng.trim();
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getFulldepartmentcode() {
        return fulldepartmentcode;
    }

    public void setFulldepartmentcode(String fulldepartmentcode) {
        this.fulldepartmentcode = fulldepartmentcode == null ? null : fulldepartmentcode.trim();
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public List<User> getUserTree() {
        return userTree;
    }

    public void setUserTree(List<User> userTree) {
        this.userTree = userTree;
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
