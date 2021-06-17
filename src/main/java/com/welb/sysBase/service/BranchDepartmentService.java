package com.welb.sysBase.service;

import com.welb.sysBase.entity.BranchDepartment;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/3
 */

public interface BranchDepartmentService {

    void insert(BranchDepartment branchDepartment);

    void deleteByParams(Map<String, String> params);

    BranchDepartment selectByDepartmentId(Integer departmentId);

    List<BranchDepartment> selectByBranchId(Integer branchId);

}
