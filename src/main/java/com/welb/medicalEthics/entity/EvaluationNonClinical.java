package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 非临床人员考核表单
 * @author: luox
 * @date： 2020/11/17 10:18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationNonClinical {

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

    /**
     * 政治面貌字典名
     */
    private String politicalStatusName;

    /**
     * 教育程度
     */
    private String educationLevel;

    /**
     * 教育程度字典名字
     */
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
    private Integer deptHeadOpinion;

    /**
     * 党支部综合意见
     * 1-优秀 2-良好 3-一般 4-较差
     */
    private Integer branchOpinion;

    /**
     * 步骤 默认0
     * 1-填写自评 2-部门负责人 3-支部意见
     */
    private Integer step;

    /**
     * 职称
     */
    private String title;

    /**
     * 职称字典值
     */
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
     * 评分等级
     */
    private Integer level;

    /**
     * 总分
     */
    private Integer score;

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

    /**
     * 当前优秀率
     */
    private String currentExcellentPercent;

    /**
     * 最大百分比
     */
    private String maxExcellentPercent;

    /**
     * 是否为暂存
     */
    private Integer isTemp;

    /**
     * 人员类型
     */
    private Integer personType;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 科室名字
     */
    private String departmentName;

}
