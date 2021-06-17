package com.welb.survey.info;

/**
 * @author luoyaozu
 * @title: OptionInfo
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查--选项json数组字段
 * @date 2020/4/1710:09
 */
public class OptionInfo {
    private Integer id;

    private String name;

    private Integer gap;

    private String gaptext;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
