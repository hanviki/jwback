package com.welb.medicalEthics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: luox
 * @date： 2021/1/5
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultVo {

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private int sex;

    private String sexName;

    /**
     * 职称
     */
    private int title;

    /**
     * 职称名字
     */
    private String titleName;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 政治面貌名称
     */
    private String politicalStatusName;

    /**
     * 科室名字
     */
    private String departmentName;

    /**
     * 聘用时间
     */
    private String hireDate;

    /**
     * 党支部信息
     */
    private String partyBranchName;

    /**
     * 党支部信息
     */
    private String generalBranchName;
    /**
     * 党委名名字
     */
    private String committeesName;
    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 人员类型
     */
    private String personType;

    /**
     * 当前步骤
     */
    private Integer step;

    /**
     * 打分书记名字
     */
    private String partyDirectorName;

    /**
     * 科室主管名字
     */
    private String departmentDirectorName;

    /**
     * 教育程度名字
     */
    private String educationLevelName;

    private String educationLevel;

    /**
     * 评分等级
     */
    private int scoreLevel;

    /**
     * 考核年份
     */
    private String year;

    /**
     * 自我评分总分
     */
    private int totalSelfScore;

    /**
     * 部门评分总分
     */
    private int totalBranchScore;

    /**
     * 科室主任总分
     */
    private int totalHeadScore;

    /**
     * 出生年月
     */
    private String birth;

    /**
     * 总分
     */
    private int score;

    /**
     * 年龄
     */
    private int age;

    /**
     * 等级
     */
    private int level;

    /**
     * 评分等级名字
     */
    private String levelName;

    private int isFinish;

    private String branchId;

    private String generalBranchId;

    private String partyCommitteesId;

}
