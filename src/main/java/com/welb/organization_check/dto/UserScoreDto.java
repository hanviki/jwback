package com.welb.organization_check.dto;

import java.util.Date;

/**
 * @author luoyaozu
 * @title: UserEvaluationDto
 * @projectName xh-360appraisal-interface
 * @description: 用户和评估报告dto
 * @date 2019/7/1111:41
 */
public class UserScoreDto {
    private String year;

    private String month;

    private String scoredCode;

    private String scorringCode;

    private String scoreType;

    private String mserialNo;

    private String fserialNo;

    private String dserialNo;

    private String dutyType;

    private Double ratio;

    private Double score;


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getScoredCode() {
        return scoredCode;
    }

    public void setScoredCode(String scoredCode) {
        this.scoredCode = scoredCode == null ? null : scoredCode.trim();
    }

    public String getScorringCode() {
        return scorringCode;
    }

    public void setScorringCode(String scorringCode) {
        this.scorringCode = scorringCode == null ? null : scorringCode.trim();
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType == null ? null : scoreType.trim();
    }

    public String getFSerialNo() {
        return fserialNo;
    }

    public void setFSerialNo(String fserialNo) {
        this.fserialNo = fserialNo == null ? null : fserialNo.trim();
    }

    public String getMserialNo() {
        return mserialNo;
    }

    public void setMserialNo(String mserialNo) {
        this.mserialNo = mserialNo == null ? null : mserialNo.trim();
    }

    public String getDserialNo() {
        return dserialNo;
    }

    public void setDserialNo(String dserialNo) {
        this.dserialNo = dserialNo == null ? null : dserialNo.trim();
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType == null ? null : dutyType.trim();
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
