package com.welb.survey.entity;

public class SurveyOption {
    private Integer id;

    private Integer surveyinfoid;

    private Integer questionid;

    private String optioncontent;

    private Integer optionorder;

    private Integer gap;

    private String gaptext;

    //有效填写答题人数-选项
    private Integer count;

    //选项占比
    private String ratio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public String getOptioncontent() {
        return optioncontent;
    }

    public void setOptioncontent(String optioncontent) {
        this.optioncontent = optioncontent;
    }

    public Integer getOptionorder() {
        return optionorder;
    }

    public void setOptionorder(Integer optionorder) {
        this.optionorder = optionorder;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public Integer getGap() {
        return gap;
    }

    public void setGap(Integer gap) {
        this.gap = gap;
    }

    public String getGaptext() {
        return gaptext;
    }

    public void setGaptext(String gaptext) {
        this.gaptext = gaptext;
    }
}
