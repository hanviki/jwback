package com.welb.medicalEthics.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalEthicsUser {

    private int id;

    /**
     * 发薪号
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 性别
     */
    private int sex;

    /**
     * 党委id
     */
    private int partyCommitteesId;

    /**
     * 党委名
     */
    private String partyCommitteesName;

    /**
     * 党委人员类型
     */
    private int partyCommitteesPersonType;

    /**
     * 党总支id
     */
    private int generalBranchId;

    /**
     * 党总支名
     */
    private String generalBranchName;

    /**
     * 党总支人员类型
     */
    private int generalBranchPersonType;

    /**
     * 党支部id
     */
    private int branchId;

    /**
     * 党支部名
     */
    private String branchName;

    /**
     * 党支部人员类型
     */
    private int branchPersonType;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 文化程度
     */
    private String educationLevel;

    /**
     * 现聘职位
     */
    private String currentPosition;

    /**
     * 聘用日期
     */
    private String hireDate;

    /**
     * 工作内容
     */
    private String jobContent;

    /**
     * 角色代号
     */
    private String roleCode;

    /**
     * 角色类型名
     */
    private String roleName;

    /**
     * 人员类型,仅供展示(以上三个类型人员类型处理的结果)
     */
    private Integer personType;

    /**
     * 是否为临时用户
     */
    private Integer isTemp;

    /**
     * 职称
     */
    private String title;

    /**
     * 出生日期
     */
    private String birth;

    /**
     * 删除状态 0-未删除  1-已经删除
     */
    private int isDelete;

    private List<String> roleList;

    private String departmentName;

    private Integer departmentId;


    public MedicalEthicsUser(String userId, String userName, int sex, int partyCommitteesId, int generalBranchId, int branchId, String idCard, String politicalStatus, String educationLevel, String currentPosition, String hireDate, String jobContent, int isDelete, String title, Integer personType) {
        this.userId = userId;
        this.userName = userName;
        this.sex = sex;
        this.partyCommitteesId = partyCommitteesId;
        this.generalBranchId = generalBranchId;
        this.branchId = branchId;
        this.idCard = idCard;
        this.politicalStatus = politicalStatus;
        this.educationLevel = educationLevel;
        this.currentPosition = currentPosition;
        this.hireDate = hireDate;
        this.jobContent = jobContent;
        this.isDelete = isDelete;
        this.title = title;
        this.personType = personType;
    }

}
