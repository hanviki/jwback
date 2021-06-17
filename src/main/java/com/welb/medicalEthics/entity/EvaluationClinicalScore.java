package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 临床评分表单
 * @author: luox
 * @date： 2020/11/18 16:48
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationClinicalScore {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 步骤 填写进度 0-初始化 1-自评完成 2-领导完成 3-党委完成
     */
    private Integer step;

    /**
     * 题目id
     */
    private Integer item;

    /**
     * 自我评分
     */
    private Integer selfScore;

    /**
     * 领导评分
     */
    private Integer headScore;

    /**
     * 党委评分
     */
    private Integer branchScore;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 题目
     */
    private String question;

    /**
     * 每个题目的最高分
     */
    private Integer maxScore;

    /**
     * 年份
     */
    private String year;

    /*
    自我评分提交人
     */
    private String selfSubmitName;

    /*
    自我评分提交时间
     */
    private String selfSubmitTime;

    /*
    主任评分提交人
     */
    private String headSubmitName;

    /*
    主任评分提交时间
     */
    private String headSubmitTime;

    /*
    书记评分提交人
     */
    private String branchSubmitName;

    /*
    书记评分提交时间
     */
    private String branchSubmitTime;
}
