package com.welb.survey.info;

/**
 * @author luoyaozu
 * @title: AnswerJson
 * @projectName xh-360appraisal-interface
 * @description: 答案json字符串
 * @date 2020/5/816:45
 */
public class AnswerJson {
    private Integer questionid;

    private String content;

    private String optionString;

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
        this.content = content;
    }

    public String getOptionString() {
        return optionString;
    }

    public void setOptionString(String optionString) {
        this.optionString = optionString;
    }
}
