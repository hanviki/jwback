package com.welb.survey.entity;

public class Answer {
    private Integer id;

    private String moneycard;

    private Integer surveyinfoid;

    private Integer questionid;

    private String content;

    private Integer optionid;

    private String submittime;

    private String gaptext;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard == null ? null : moneycard.trim();
    }

    public Integer getSurveyinfoid() {
        return surveyinfoid;
    }

    public void setSurveyinfoid(Integer surveyinfoid) {
        this.surveyinfoid = surveyinfoid;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getOptionid() {
        return optionid;
    }

    public void setOptionid(Integer optionid) {
        this.optionid = optionid;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime == null ? null : submittime.trim();
    }

    public String getGaptext() {
        return gaptext;
    }

    public void setGaptext(String gaptext) {
        this.gaptext = gaptext;
    }
}
