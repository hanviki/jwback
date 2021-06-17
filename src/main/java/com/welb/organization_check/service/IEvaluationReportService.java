package com.welb.organization_check.service;

import com.welb.organization_check.dto.UserEvaluationDto;
import com.welb.organization_check.entity.EvaluationReport;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IEvaluationReportService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/199:44
 */
public interface IEvaluationReportService {
    /**
     * 删除个人测评报告
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加个人测评报告
     * @param record
     * @return
     */
    int insertSelective(EvaluationReport record);

    /**
     * 修改个人测评报告
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(EvaluationReport record);

    /**
     * 查询个人测评报告
     * @param id
     * @return
     */
    EvaluationReport selectByPrimaryKey(Integer id);

    /**
     * 查找上一季度的测评报告
     * @param report
     * @return
     */
    EvaluationReport selectReportByUserCode(EvaluationReport report);

    /**
     * 计算个人测评表的总分
     * @return
     */
    Double selectReportTotalScore(String year,String month);

    /**
     * 获取数据总量
     * @return
     */
    int selectReportCount(String year,String month);
    /**
     * 获取数据总量
     * @return
     */
    int selectMaxAndMinReportCount(String year,String month);

    /**
     * 查询所有人员评估报告
     * @return
     */
    List<EvaluationReport> selectAllEvaluationReport();

    /**
     *
     * @param evaluationDto
     * @return
     */
    List<UserEvaluationDto>selectAllEvaluationReportLike(UserEvaluationDto evaluationDto);
    /**
     * 查找数据总量
     * @return
     */
    int selectEvacationReportCount();

    /**
     * 获取最高分总和
     * @return
     */
    Double selectSumOfMaxScore(String year,String month);

    /**
     * 获取最低分总和
     * @return
     */
    Double selectSumOfMinScore(String year,String month);


    /**
     * 查询主键最大数
     * @return
     */
    int selectMaxId();

    /**
     * 通过用户编号查找evaluationreport数据
     * @param usercode
     * @return
     */

    List<EvaluationReport>selectEvaluationByUserCode(String usercode);


    /**
     * 批量删除
     * @param reportIds
     * @return
     */
    int batchDelete(List<Integer>reportIds);


    int updateAvgScore(double avgscore,String year,String month);







}
