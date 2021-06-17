package com.welb.personnel_check.service;


import com.welb.personnel_check.entity.PersonnelUser;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IPersonnelUserService
 * @projectName xh-360appraisal-interface
 * @description: 人事部用户业务层接口
 * @date 2019/9/211:06
 */
public interface IPersonnelUserService {
    /**
     * 添加用户
     * @param personnelUser
     * @return
     */
    int insertSelective(PersonnelUser personnelUser);

    /**
     * 多条件查询人事部用户数据
     * @param personnelUser
     * @return
     */
    List<PersonnelUser> selectAllPersonnelByLike(PersonnelUser personnelUser);

    /**
     * 删除用户
     * @param moneycard
     * @return
     */
    int deleteByPrimaryKey(String moneycard);


    /**
     * 查找用户数据
     * @param moneycard
     * @return
     */
    PersonnelUser selectByPrimaryKey(String moneycard);


    /**
     * 查找所有用户数据
     * @return
     */
    List<PersonnelUser>selectList();


    /**
     * 通过部门名称查找所有用户数据
     * @param departmentname
     * @return
     */
    List<PersonnelUser>selectListByDeptName(String departmentname);


    int updateByPrimaryKeySelective(PersonnelUser personnelUser);

    /**
     * 批量添加用户
     * @param list
     * @return
     */
    int batchInsert(List<PersonnelUser>list);

    /**
     * 通过部门名称查找数据
     * @param departmentname
     * @return
     */

    List<PersonnelUser>selectPersonnelByDepartmentName(String departmentname);


    List<PersonnelUser>selectListByPersonner(PersonnelUser personnelUser);

    /**
     * 通过发薪号修改代理状态
     * @param flag
     * @param moneycard
     * @return
     */
    int updateFlagByMoneyCard(String flag,String moneycard);


    int updateFlag(String flag,String moneycard);

    /**
     * 通过主键查找数据
     * @param id
     * @return
     */
    PersonnelUser selectById(Integer id);

    List<String> allUserIds();
}
