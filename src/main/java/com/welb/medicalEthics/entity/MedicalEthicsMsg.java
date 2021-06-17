package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEthicsMsg {
    private int id;
    private String userId;  //发薪号
    private int partyCommitteesId;  //党委id
    private String partyCommitteesName;  //党委名
    private int partyCommitteesPersonType;  //党委人员类型
    private int generalBranchId;  //党总支id
    private String generalBranchName;  //党总支名
    private int generalBranchPersonType;  //党总支人员类型
    private int branchId;  //党支部id
    private String branchName;  //党支部名
    private int branchPersonType;  //党支部人员类型
    private String userName;
    private int isDelete;
    private int personType;  //人员类型,仅供展示(以上三个类型人员类型处理的结果)
    private int isFinished;
    private int isSelfEvaluate;
    private int step;
    /**
     * 评分等级
     */
    private int scoreLevel;
    
    /**
     * 是否可以进行考核
     */
    private int isEditable;


    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public MedicalEthicsMsg(String userId, int partyCommitteesId, int generalBranchId, int branchId, String userName, int isDelete,int personType) {
        this.userId = userId;
        this.partyCommitteesId = partyCommitteesId;
        this.generalBranchId = generalBranchId;
        this.branchId = branchId;
        this.userName = userName;
        this.isDelete = isDelete;
        this.personType = personType;
    }

    public int getPartyCommitteesId() {
        return partyCommitteesId;
    }

    public void setPartyCommitteesId(int partyCommitteesId) {
        this.partyCommitteesId = partyCommitteesId;
    }

    public String getPartyCommitteesName() {
        return partyCommitteesName;
    }

    public void setPartyCommitteesName(String partyCommitteesName) {
        this.partyCommitteesName = partyCommitteesName;
    }

    public int getPartyCommitteesPersonType() {
        return partyCommitteesPersonType;
    }

    public void setPartyCommitteesPersonType(int partyCommitteesPersonType) {
        this.partyCommitteesPersonType = partyCommitteesPersonType;
    }

    public int getGeneralBranchId() {
        return generalBranchId;
    }

    public void setGeneralBranchId(int generalBranchId) {
        this.generalBranchId = generalBranchId;
    }

    public String getGeneralBranchName() {
        return generalBranchName;
    }

    public void setGeneralBranchName(String generalBranchName) {
        this.generalBranchName = generalBranchName;
    }

    public int getGeneralBranchPersonType() {
        return generalBranchPersonType;
    }

    public void setGeneralBranchPersonType(int generalBranchPersonType) {
        this.generalBranchPersonType = generalBranchPersonType;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getBranchPersonType() {
        return branchPersonType;
    }

    public void setBranchPersonType(int branchPersonType) {
        this.branchPersonType = branchPersonType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
