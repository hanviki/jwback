package com.welb.medicalEthics.service.impl;

import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.mapper.BranchDepartmentMapper;
import com.welb.sysBase.service.BranchDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/7
 */

@Service
public class BranchDepartmentServiceImpl implements BranchDepartmentService {

    @Autowired
    private BranchDepartmentMapper branchDepartmentMapper;

    @Override
    public void insert(BranchDepartment branchDepartment) {

    }

    @Override
    public void deleteByParams(Map<String, String> params) {

    }

    @Override
    public BranchDepartment selectByDepartmentId(Integer departmentId) {
        return branchDepartmentMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<BranchDepartment> selectByBranchId(Integer branchId) {
        return branchDepartmentMapper.selectByBranchId(branchId);
    }
}
