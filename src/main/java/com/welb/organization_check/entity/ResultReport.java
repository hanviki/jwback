package com.welb.organization_check.entity;

public class ResultReport {
    private Integer id;

    private String resultreportname;

    private String resultreportcode;

    private Integer evaluationreportcode;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;

    private Double score;

    private Double avgscore;

    private Double maxscore;

    private Double minscore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResultreportname() {
        return resultreportname;
    }

    public void setResultreportname(String resultreportname) {
        this.resultreportname = resultreportname == null ? null : resultreportname.trim();
    }

    public Integer getEvaluationreportcode() {
        return evaluationreportcode;
    }

    public void setEvaluationreportcode(Integer evaluationreportcode) {
        this.evaluationreportcode = evaluationreportcode;
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

    public String getResultreportcode() {
        return resultreportcode;
    }

    public void setResultreportcode(String resultreportcode) {
        this.resultreportcode = resultreportcode;
    }

    public ResultReport() {
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
}
