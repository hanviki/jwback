package com.welb.survey.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: BatchJson
 * @projectName xh-360appraisal-interface
 * @description: 批量添加题目json串对象
 * @date 2020/5/716:15
 */
public class BatchJson {
    private String title;

    private Integer type;

    private String nav;//选项json字符串

    private Integer questionid;

    private boolean editShow;

    private String answer;

    private Integer iswrite;

    //分组设置
    private int multiple;

    //分组设置内容
    private String multipletext;

    private Integer questionorder;

    private boolean gap;

    private String gaptext;

    private List<OptionInfo>optionInfoList=new ArrayList<>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public boolean isEditShow() {
        return editShow;
    }

    public void setEditShow(boolean editShow) {
        this.editShow = editShow;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<OptionInfo> getOptionInfoList() {
        return optionInfoList;
    }

    public void setOptionInfoList(List<OptionInfo> optionInfoList) {
        this.optionInfoList = optionInfoList;
    }

    public Integer getIswrite() {
        return iswrite;
    }

    public void setIswrite(Integer iswrite) {
        this.iswrite = iswrite;
    }

    public Integer getQuestionorder() {
        return questionorder;
    }

    public void setQuestionorder(Integer questionorder) {
        this.questionorder = questionorder;
    }

    public boolean isGap() {
        return gap;
    }

    public void setGap(boolean gap) {
        this.gap = gap;
    }

    public String getGaptext() {
        return gaptext;
    }

    public void setGaptext(String gaptext) {
        this.gaptext = gaptext;
    }
}
