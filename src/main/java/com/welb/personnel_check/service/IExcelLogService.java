package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.ExcelLog;

/**
 * @author luoyaozu
 * @title: IExcelLogService
 * @projectName xh-360appraisal-interface
 * @description: 上传分数excel的行为日志业务层接口
 * @date 2020/4/2610:10
 */
public interface IExcelLogService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ExcelLog log);

    ExcelLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExcelLog log);
}
