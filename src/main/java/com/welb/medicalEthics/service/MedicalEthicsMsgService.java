package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;

import java.util.List;
import java.util.Map;

public interface MedicalEthicsMsgService {

    List<MedicalEthicsMsg> list(Map<String,String> params);

    List<MedicalEthicsMsg> getList(Map<String,String> params);

    void delete(String userId);

    void deleteByUserId(String userId);

    void update(MedicalEthicsMsg medicalEthicsMsg);

    List<String> insert(MedicalEthicsMsg medicalEthicsMsg);

    List<String> batchInsert(List<MedicalEthicsMsg> dataSource);

    List<String> doSubmit(List<Integer> idList);

    MedicalEthicsMsg selectByUserId(String userId);

    MedicalEthicsMsg selectById(Integer id);

    List<MedicalEthicsMsg> selectByBranchId(Integer branchId);

    List<MedicalEthicsMsg> selectAll(Map<String,String> params);

    List<String> allIdList();

    List<MedicalEthicsMsg> showMissingUsers();

    List<MedicalEthicsMsg> selectCommonUser(String branchId);

    /**
     * updateMsg 更新已提交人员
     *
     * @param userId userId
     * @param departmentId departmentId
     * @param personType personType
     *
     */
    void updateMsg(String userId,String departmentId,String personType);
}
