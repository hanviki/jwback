package com.welb.sysBase.service;

import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.EvaluationDepartment;

import java.util.List;
import java.util.Map;

/**
 * @description: 科室管理
 * @author: luox
 * @date： 2020/12/3
 */

public interface EvaluationDepartmentService {

    EvaluationDepartment selectById(Integer id);

    List<EvaluationDepartment> list(Map<String, String> params);

    void update(EvaluationDepartment department);

    void delete(String departmentId);

    void insert(EvaluationDepartment department);

    void insertDepartment(EvaluationDepartment department);

    EvaluationDepartment getDetailById(Integer id);

    /**
     * 扩展基本信息
     * @param departmentList
     * @return
     */
    List<EvaluationDepartment> extendBranchInfo(List<EvaluationDepartment> departmentList);

    Boolean checkDeleteStatus(Integer id);

    boolean updateUserInfo(DepartmentUserVo departmentUserVo);

    List<EvaluationDepartment> listAll();

    String getDepartmentNameByUserId(String userId);

    String selectDirectorNameByDepartmentId(int departmentId);

}
