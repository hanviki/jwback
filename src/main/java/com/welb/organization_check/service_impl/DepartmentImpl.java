package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Department;
import com.welb.organization_check.mapper.DepartmentMapper;
import com.welb.organization_check.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DepartmentImpl
 * @projectName kao
 * @description: 部门业务层接口的实现类
 * @date 2019/5/1621:47
 */
@Service
@Transactional
public class DepartmentImpl implements IDepartmentService
{
    @Resource
    DepartmentMapper departmentMapper;
    @Override
    public int deleteByDeptCode(String departmentcode) {
        return departmentMapper.deleteByPrimaryKey(departmentcode);
    }

    @Override
    public int addDepartment(Department department) {
        String departmentCode = departmentMapper.selectMaxDepartmentCode();
        if (departmentCode==null){
            department.setDepartmentcode("100");
        }else {
            //将获得的最大部门编号转换成整数类型
            int num = Integer.parseInt(departmentCode.trim());
            //主键加1
            num++;
            //将自增的主键再转成字符类型
            String code = String.valueOf(num);
            department.setDepartmentcode(code);
        }
        return departmentMapper.insertSelective(department);
    }

    @Override
    public Department selectByDeptCode(String departmentcode) {
        return departmentMapper.selectByPrimaryKey(departmentcode);
    }

    @Override
    public int updateByDeptCode(Department department) {
        return departmentMapper.updateByPrimaryKeySelective(department);
    }

    @Override
    public List<Department> selectDepartmentTrees(Department department) {
        return departmentMapper.selectDepartmentTrees(department);
    }

}
