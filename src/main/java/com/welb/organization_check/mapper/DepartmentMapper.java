package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    int deleteByPrimaryKey(String departmentcode);

    int insertSelective(Department record);

    int updateByPrimaryKeySelective(Department department);

    Department selectByPrimaryKey(String departmentcode);

    List<Department>selectDepartmentTrees(Department department);

    String selectMaxDepartmentCode();

}
