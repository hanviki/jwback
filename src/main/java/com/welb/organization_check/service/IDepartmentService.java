package com.welb.organization_check.service;

import com.welb.organization_check.entity.Department;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IDepartmentService
 * @projectName kao
 * @description: 部门业务层接口
 * @date 2019/5/1621:16
 */

public interface IDepartmentService {
    /**
     * 通过DepartmentCode删除部门
     * @param departmentcode
     * @return
     */
    int deleteByDeptCode(String departmentcode);

    /**
     * 添加部门
     * @param department
     * @return
     */
    int addDepartment(Department department);

    /**
     * 通过DepartmentCode查找部门
     * @param departmentcode
     * @return
     */
    Department selectByDeptCode(String departmentcode);

    /**
     * 通过DepartmentCode修改部门信息
     * @param department
     * @return
     */
    int updateByDeptCode(Department department);

    /**
     * 查询所有部门
     * @return
     */
    List<Department>selectDepartmentTrees(Department department);

}
