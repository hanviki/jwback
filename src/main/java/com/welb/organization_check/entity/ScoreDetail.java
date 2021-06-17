// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ScoreDetail.java

package com.welb.organization_check.entity;


public class ScoreDetail
{

    private String serialNo;
    private String FSerialNo;
    private String DSerialNo;
    private String score;

    public ScoreDetail()
    {
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFSerialNo()
    {
        return FSerialNo;
    }

    public void setFSerialNo(String FSerialNo)
    {
        this.FSerialNo = FSerialNo != null ? FSerialNo.trim() : null;
    }

    public String getDSerialNo()
    {
        return DSerialNo;
    }

    public void setDSerialNo(String DSerialNo)
    {
        this.DSerialNo = DSerialNo != null ? DSerialNo.trim() : null;
    }

    public String getScore()
    {
        return score;
    }

    public void setScore(String score)
    {
        this.score = score != null ? score.trim() : null;
    }

}
