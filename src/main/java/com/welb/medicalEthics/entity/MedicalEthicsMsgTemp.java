package com.welb.medicalEthics.entity;

public class MedicalEthicsMsgTemp {
    private int id;
    private String userId;  //发薪号
    private int partyCommitteesId;  //党委id
    private String partyCommitteesName;  //党委名
    private int generalBranchId;  //党总支id
    private String generalBranchName;  //党总支
    private int branchId;  //党支部id
    private String branchName;  //党支部名
    private String userName;  //姓名
    private int status;  //是否提交
    private Integer personType; // 临时表人员类型:0-临床人员，1-非临床人员

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public MedicalEthicsMsgTemp() {
    }

    public MedicalEthicsMsgTemp(int id, String userId, int partyCommitteesId, int generalBranchId, int branchId, String userName, int status, int personType) {
        this.id = id;
        this.userId = userId;
        this.partyCommitteesId = partyCommitteesId;
        this.generalBranchId = generalBranchId;
        this.branchId = branchId;
        this.userName = userName;
        this.status = status;
        this.personType = personType;
    }

    public MedicalEthicsMsgTemp(String userId, int partyCommitteesId, int generalBranchId, int branchId, String userName, int status, int personType) {
        this.userId = userId;
        this.partyCommitteesId = partyCommitteesId;
        this.generalBranchId = generalBranchId;
        this.branchId = branchId;
        this.userName = userName;
        this.status = status;
        this.personType = personType;
    }
}
