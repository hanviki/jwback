package com.welb.organization_check.entity;

import java.util.Date;

public class ScoreFlow {
    private String serialno;

    private String mserialno;

    private String scoredcode;

    private String scorringcode;

    private Date scoredate;

    private String scoretype;

    private Integer state;

    private Double score;

    private Double ratio;

    private String Scorringname;

    private String singlescore;

    private String dutyname;

    private  String stationname;

    private String deparmentname;

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno == null ? null : serialno.trim();
    }

    public String getMserialno() {
        return mserialno;
    }

    public void setMserialno(String mserialno) {
        this.mserialno = mserialno == null ? null : mserialno.trim();
    }

    public void setScoredcode(String scoredcode) {
        this.scoredcode = scoredcode == null ? null : scoredcode.trim();
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode == null ? null : scorringcode.trim();
    }

    public Date getScoredate() {
        return scoredate;
    }

    public void setScoredate(Date scoredate) {
        this.scoredate = scoredate;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getScoredcode() {
        return scoredcode;
    }

    public String getScorringname() {
        return Scorringname;
    }

    public void setScorringname(String scorringname) {
        Scorringname = scorringname;
    }

    public String getSinglescore() {
        return singlescore;
    }

    public void setSinglescore(String singlescore) {
        this.singlescore = singlescore;
    }

    public String getDutyname() {
        return dutyname;
    }

    public void setDutyname(String dutyname) {
        this.dutyname = dutyname;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getDeparmentname() {
        return deparmentname;
    }

    public void setDeparmentname(String deparmentname) {
        this.deparmentname = deparmentname;
    }
}
