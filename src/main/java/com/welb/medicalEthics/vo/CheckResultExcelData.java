package com.welb.medicalEthics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 打分结果导出
 * @author: luox
 * @date： 2021/1/12
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultExcelData {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 出生年月
     */
    private String birth;

    /**
     * 政治面貌名称
     */
    private String politicalStatusName;

    /**
     * 教育程度名字
     */
    private String educationLevelName;

    private String sexName;

    /**
     * 职称名字
     */
    private String titleName;

    /**
     * 聘用时间
     */
    private String hireDate;

    /**
     * 科室名字
     */
    private String departmentName;

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
     * 人员类型
     */
    private String personType;

    /**
     * 打分书记名字
     */
    private String partyDirectorName;

    /**
     * 科室主管名字
     */
    private String departmentDirectorName;

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
     * 总分
     */
    private int score;

    /**
     * 年龄
     */
    private int age;


    /**
     * 评分等级名字
     */
    private String levelName;

}
