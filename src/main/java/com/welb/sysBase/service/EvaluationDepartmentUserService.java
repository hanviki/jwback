package com.welb.sysBase.service;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.vo.CheckResultVo;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.EvaluationDepartmentUser;

import java.util.List;
import java.util.Map;

/**
 * @description: 部门-用户关联
 * @author: luox
 * @date： 2020/12/7
 */

public interface EvaluationDepartmentUserService {

    void insert(EvaluationDepartmentUser departmentUser);

    void batchInsert(List<EvaluationDepartmentUser> list);

    void batchDelete(List<String> ids);

    void deleteByParams(Map<String,String> params);

    List<EvaluationDepartmentUser> selectByUserId(Integer userId);

    List<EvaluationDepartmentUser> selectByDepartmentId(Integer departmentId);

    List<DepartmentUserVo> selectUserDetailByDepartmentId(Map<String,String> params);

    List<DepartmentUserVo> selectUserDetailByBranchId(Map<String,String> params);

    List<DepartmentUserVo> selectUserDetailByParams(Map<String,Object> params);

    void autoDepartment(List<MedicalEthicsMsg> medicalEthicsMsgList);

    List<MedicalEthicsMsg> selectMsgList(Map<String,String> prams);

    List<CheckResultVo> selectCheckDetailByParams(Map<String,Object> params);

    /**
     * 新用户添加绑定
     * @param departmentUser
     */
    void bindNewUser(EvaluationDepartmentUser departmentUser);

    List<CheckResultVo> checkResultByType(Map<String, Object> params);

}
