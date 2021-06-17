package com.welb.survey.entity;

import java.util.List;

public class SurveyInfoPreview {
    private Integer id;

    private String title;

    private String content="";

    private String batchjson;

    //问卷调查与问题是一对多关系
    private List<SurveyQuestion> questionList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getBatchjson() {
        return batchjson;
    }

    public void setBatchjson(String batchjson) {
        this.batchjson = batchjson == null ? null : batchjson.trim();
    }

    public List<SurveyQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SurveyQuestion> questionList) {
        this.questionList = questionList;
    }
}
