package com.welb.organization_check.entity;

public class ResultDetail {
    private Integer id;

    private Integer resultreportcode;

    private String dutyname;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;

    private Double score;

    private Double avgscore;

    private Integer orderid;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResultreportcode() {
        return resultreportcode;
    }

    public void setResultreportcode(Integer resultreportcode) {
        this.resultreportcode = resultreportcode;
    }

    public String getDutyname() {
        return dutyname;
    }

    public void setDutyname(String dutyname) {
        this.dutyname = dutyname == null ? null : dutyname.trim();
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(Double avgscore) {
        this.avgscore = avgscore;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
}
