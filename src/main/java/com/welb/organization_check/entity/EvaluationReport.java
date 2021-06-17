package com.welb.organization_check.entity;

public class EvaluationReport {
    private Integer id;

    private String usercode;

    private Double totalscore;

    private Double basicscore;

    private Double keyscore;

    private Double avgscore;

    private Double maxscore;

    private Double minscore;

    private Double scoredifference;

    private Double totalcomparelast;

    private Double totalcomparemark;

    private String year;

    private String month;

    private Double maxcomparelast;

    private Double maxcomparemark;

    private Double mincomparelast;

    private Double mincomparemark;

    private Double plan;//进度

    //单纯添加额外属性  方便获取
    private String username;

    private String stationname;

    private String departmentname;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;


    private String rolecode;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public Double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(Double totalscore) {
        this.totalscore = totalscore;
    }

    public Double getBasicscore() {
        return basicscore;
    }

    public void setBasicscore(Double basicscore) {
        this.basicscore = basicscore;
    }

    public Double getKeyscore() {
        return keyscore;
    }

    public void setKeyscore(Double keyscore) {
        this.keyscore = keyscore;
    }

    public Double getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(Double avgscore) {
        this.avgscore = avgscore;
    }

    public Double getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(Double maxscore) {
        this.maxscore = maxscore;
    }

    public Double getMinscore() {
        return minscore;
    }

    public void setMinscore(Double minscore) {
        this.minscore = minscore;
    }

    public Double getScoredifference() {
        return scoredifference;
    }

    public void setScoredifference(Double scoredifference) {
        this.scoredifference = scoredifference;
    }

    public Double getTotalcomparelast() {
        return totalcomparelast;
    }

    public void setTotalcomparelast(Double totalcomparelast) {
        this.totalcomparelast = totalcomparelast;
    }

    public Double getTotalcomparemark() {
        return totalcomparemark;
    }

    public void setTotalcomparemark(Double totalcomparemark) {
        this.totalcomparemark = totalcomparemark;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public Double getMaxcomparelast() {
        return maxcomparelast;
    }

    public void setMaxcomparelast(Double maxcomparelast) {
        this.maxcomparelast = maxcomparelast;
    }

    public Double getMaxcomparemark() {
        return maxcomparemark;
    }

    public void setMaxcomparemark(Double maxcomparemark) {
        this.maxcomparemark = maxcomparemark;
    }

    public Double getMincomparelast() {
        return mincomparelast;
    }

    public void setMincomparelast(Double mincomparelast) {
        this.mincomparelast = mincomparelast;
    }

    public Double getMincomparemark() {
        return mincomparemark;
    }

    public void setMincomparemark(Double mincomparemark) {
        this.mincomparemark = mincomparemark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public Double getAscore() {
        return ascore;
    }

    public void setAscore(Double ascore) {
        this.ascore = ascore;
    }

    public Double getBscore() {
        return bscore;
    }

    public void setBscore(Double bscore) {
        this.bscore = bscore;
    }

    public Double getCscore() {
        return cscore;
    }

    public void setCscore(Double cscore) {
        this.cscore = cscore;
    }

    public Double getDscore() {
        return dscore;
    }

    public void setDscore(Double dscore) {
        this.dscore = dscore;
    }

    public Double getPlan() {
        return plan;
    }

    public void setPlan(Double plan) {
        this.plan = plan;
    }

    public String getRolecode() {

        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }
}
