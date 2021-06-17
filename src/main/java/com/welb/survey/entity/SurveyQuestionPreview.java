package com.welb.survey.entity;

import java.util.List;

public class SurveyQuestionPreview {
    private Integer id;

    private Integer surveyinfoid;

    private String questiontitle;

    private Integer type;

    private String updatetime;

    private Integer questionorder;

    private Integer iswrite;

    //分组设置
    private int multiple;

    //分组设置内容
    private String multipletext;

    //有效填写答题人数-问题
    private int count;

    //问题和选项是一对多的关系
    private List<SurveyOption> optionList;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public List<SurveyOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<SurveyOption> optionList) {
        this.optionList = optionList;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public void setQuestiontitle(String questiontitle) {
        this.questiontitle = questiontitle;
    }

    public Integer getQuestionorder() {
        return questionorder;
    }

    public void setQuestionorder(Integer questionorder) {
        this.questionorder = questionorder;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getIswrite() {
        return iswrite;
    }

    public void setIswrite(Integer iswrite) {
        this.iswrite = iswrite;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public String getMultipletext() {
        return multipletext;
    }

    public void setMultipletext(String multipletext) {
        this.multipletext = multipletext;
    }
}
