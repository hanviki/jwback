package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 临床人员考核表单
 * @author: luox
 * @date： 2020/11/17 10:18
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationClinical {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 发薪号
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private String birth;

    /**
     * 政治面貌
     */
    private String politicalStatus;
    private String politicalStatusName;

    /**
     * 教育程度
     */
    private String educationLevel;
    private String educationLevelName;

    /**
     * 当前职位
     */
    private String currentPosition;

    /**
     * 聘用时间
     */
    private String hireDate;

    /**
     * 工作内容
     */
    private String jobContent;

    /**
     * 自我评价
     */
    private String selfSummary;

    /**
     * 部门负责人意见
     */
    private String deptHeadOpinion;

    /**
     * 党支部综合意见
     * 1-优秀 2-良好 3-一般 4-较差
     */
    private Integer branchOpinion;

    /**
     * 职称
     */
    private String title;
    private String titleName;

    /**
     * 党委id
     */
    private Integer partyCommitteesId;


    /**
     * 党委名字
     */
    private String partyCommitteesName;

    /**
     * 党总支id
     */
    private Integer generalBranchId;

    /**
     * 党总支名
     */
    private String generalBranchName;

    /**
     * 党支部id
     */
    private Integer branchId;

    /**
     * 党支部名字
     */
    private String branchName;

    /**
     * 步骤 默认0
     * 1-填写自评 2-部门负责人 3-支部意见
     */
    private Integer step;

    /**
     * 具体条目 id
     */
    private Integer item;

    /**
     * 得分评级
     */
    private Integer level;

    /**
     * 得分
     */
    private String score;

    /**
     * 考核年份
     */
    private String year;

    /**
     * 用户字段
     */
    private Integer personType;

    /**
     *已考核&未考核
     */
    private Integer isFinish;

    /**
     * 自评总分
     */
    private Integer totalSelfScore;

    /**
     * 科室主任总分
     */
    private Integer totalHeadScore;

    /**
     * 书记评分总分
     */
    private Integer totalBranchScore;

    /**
     * 部门名字
     */
    private String departmentName;

}
