package com.welb.survey.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SurveyInfo {
    public static Map<Integer, String> publishstatusList = new LinkedHashMap<>();

    static {
        //发布状态
        publishstatusList.put(1, "未发布");
        publishstatusList.put(2, "已发布");
    }

    private Integer id;

    private String title;

    private String content="";

    private Integer publishstatus;

    private String publishstatusname;

    private Integer flag;

    private Integer starstatus;

    private String link;

    private Integer answercount;

    private String createtime;

    private String updatetime;

    private String createuser;

    private String updateuser;

    private String batchjson;

    private Integer surveytype;

    private Integer previewid;



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

    public Integer getPublishstatus() {
        return publishstatus;
    }

    public void setPublishstatus(Integer publishstatus) {
        this.publishstatus = publishstatus;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getStarstatus() {
        return starstatus;
    }

    public void setStarstatus(Integer starstatus) {
        this.starstatus = starstatus;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public static Map<Integer, String> getPublishstatusList() {
        return publishstatusList;
    }

    public static void setPublishstatusList(Map<Integer, String> publishstatusList) {
        SurveyInfo.publishstatusList = publishstatusList;
    }

    public String getPublishstatusname() {
        return publishstatusList.get(publishstatus);
    }

    public void setPublishstatusname(String publishstatusname) {
        this.publishstatusname = publishstatusname;
    }

    public Integer getAnswercount() {
        return answercount;
    }

    public void setAnswercount(Integer answercount) {
        this.answercount = answercount;
    }


    public List<SurveyQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SurveyQuestion> questionList) {
        this.questionList = questionList;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }


    public String getBatchjson() {
        return batchjson;
    }

    public void setBatchjson(String batchjson) {
        this.batchjson = batchjson;
    }

    public Integer getSurveytype() {
        return surveytype;
    }

    public void setSurveytype(Integer surveytype) {
        this.surveytype = surveytype;
    }

    public Integer getPreviewid() {
        return previewid;
    }

    public void setPreviewid(Integer previewid) {
        this.previewid = previewid;
    }
}
