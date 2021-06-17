package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.MedicalEthicsRoleInfo;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;

import java.util.List;
import java.util.Map;

public interface MedicalEthicsUserService {

    List<MedicalEthicsUser> selectByUserId(String userId);

    void updateBaseInfo(MedicalEthicsUser medicalEthicsUser);

    List<MedicalEthicsUser> list(Map<String,String> params);

    List<MedicalEthicsUser> listRoles(Map<String,String> params);

    List<MedicalEthicsUserRole> showRoleMsg(Map<String,String> params);

    String getHighestPermission(String uId);

    Map<String, String> loadPermissionCondition(String uId);

    void batchInsert(List<MedicalEthicsUser> dataSource);

    void batchDelete(List<Integer> idList);

    void deleteByUserId(String userId);

    void batchInsertIntoUserRole(List<MedicalEthicsUserRole> dataSource);

    void batchDeleteUserRole(List<String> userIds);

    /**
     * 批量删除普通用户的角色
     * @param userIds
     */
    void batchDeleteCommonUserRole(List<String> userIds);

    void deleteUserRoleByUserId(String userId);

    void deleteUserRoleByUserIdAndPartyId(String userId,Integer partyId);

    MedicalEthicsRoleInfo getRoleInfo(Map<String,String> params);

    void updateRelations(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId,Integer personType);

    /**
     *
     * @param userId
     * @param partyCommitteesId
     * @param generalBranchId
     * @param branchId
     * @param personType
     */
    void updateRelations2(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId,Integer personType);


    /**
     * selectByParams 根据参数查询所有列
     *
     * @param params params
     * @return {@link List<MedicalEthicsUser>}
     *
     */
    List<MedicalEthicsUser> selectByParams(Map<String, String> params);

    void addUserRole(MedicalEthicsUserRole userRole);

    void addUser(MedicalEthicsUser user);

    List<MedicalEthicsUserRole> selectUserRoleByUserId(String userId);

    List<MedicalEthicsUserRole> selectUserRoleByParams(Map<String,String> params);

    void deleteUserRoleByParams(Map<String, String> prams);

    List<MedicalEthicsUser> listByUserId(List<String> userIdsList);
}
