package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.DeptComplete;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IDeptCompleteService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/2315:14
 */
public interface IDeptCompleteService {
    int deleteByPrimaryKey(String completecode);

    int insertSelective(DeptComplete deptComplete);

    DeptComplete selectByPrimaryKey(String completecode);

    int updateByPrimaryKeySelective(DeptComplete deptComplete);

    List<DeptComplete> selectList(DeptComplete deptComplete);

    List<DeptComplete> selectAllDeptComplete(String year,String month,String  deptname,int pageNum,int pageSize);

    DeptComplete selectSingleDept(DeptComplete deptComplete);

    DeptComplete selectByDepart(DeptComplete deptComplete);

    int deleteByYearAndMonth(String year,String month);

    List<DeptComplete>getNoImportExcelAndPdf(DeptComplete deptComplete);



}
