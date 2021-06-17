package com.welb.organization_check.service;

import com.welb.organization_check.entity.ResultReport;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IResultReportService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/200:00
 */
public interface IResultReportService {
    /**
     * 删除综合结果报告数据
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加综合结果报告数据
     * @param record
     * @return
     */
    int insertSelective(ResultReport record);

    /**
     * 查找综合结果报告数据
     * @param id
     * @return
     */
    ResultReport selectByPrimaryKey(Integer id);

    /**
     * 修改综合结果报告数据
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ResultReport record);

    /**
     * 通过个人测评表的主键查找综合报告表的数据
     * @param evaluationCode
     * @return
     */
    List<ResultReport> selectResultReportByEvaluationCode(Integer evaluationCode);
    /**
     * 通过维度编号和个人测评表的主键查找综合报告表的数据
     * @param resultreportcode
     * @param evaluationreportcode
     * @return
     */
    ResultReport selectResultReportByCode(String resultreportcode,Integer evaluationreportcode);

    /**
     * 批量删除
     * @param resultReportIds
     * @return
     */
    int batchDelete(List<Integer>resultReportIds);


}
