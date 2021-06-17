package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.DeptCheck;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IDeptCheckService
 * @projectName xh-360appraisal-interface
 * @description: 部门考核附件信息业务层接口
 * @date 2019/8/1316:40
 */
public interface IDeptCheckService {
    int insertSelective(DeptCheck deptCheck);

    int updateByPrimaryKeySelective(DeptCheck deptCheck);

    DeptCheck selectDeptCheckByYear(String year,String moneyCard);

    DeptCheck selectDeptCheckByYearAndMoneyCard(String year,String moneycard);

    DeptCheck selectDeptCheckByYearAndDepart(String year,String depart);

    List<DeptCheck> selectList(DeptCheck deptCheck);



}
