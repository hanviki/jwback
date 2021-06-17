package com.welb.organization_check.service;

import com.welb.organization_check.entity.MonthSummary;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IMonthSummaryService
 * @projectName xh-360appraisal-interface
 * @description: 个人季度总结业务层接口
 * @date 2019/5/2914:30
 */
public interface IMonthSummaryService {

    /**
     * 添加个人季度总结
     * @param summary
     * @return
     */
    int insertSelective(MonthSummary summary);

    /**
     * 修改个人季度总结
     * @param summary
     * @return
     */
    int updateByPrimaryKeySelective(MonthSummary summary);

    /**
     *  通过主键修改季结提交状态
     * @param serialno
     * @return
     */
    int updateSubmitStateBySerialNo(String serialno);
    /**
     *  通过主键修改季结评分状态
     * @param serialno
     * @return
     */
    int updateGradeStateBySerialNo(String serialno);
    /**
     * 季结状态全部修改为季节评分
     * @return
     */
    int updateStateAll();

    /**
     * 批量修改状态为季节评分完成
     * @param serialno
     * @return
     */
    int updateFinishGradeBySerialNo(String serialno);

    /**
     * 将状态全部修改为季节评分完成
     * @return
     */
    int updateFinishGradeAll();

    /**
     * 删除个人季度总结
     * @param serialno
     * @return
     */

    int deleteByPrimaryKey(String serialno);

    /**
     * 通过季节编码查找个人季节总结
     * @param serialno
     * @return
     */
    MonthSummary selectByPrimaryKey(String serialno);


    /**
     * 查询当前登录用户的所有个人季节总结
     * @param summary
     * @return
     */
    List<MonthSummary> selectSummaryLikeAll(MonthSummary summary);

    /**
     * 修改状态
     * @param summary
     * @return
     */
    int updateStateBySerialNo(MonthSummary summary);

    /**
     * 通过季度和年份查找季度总结（短信已发送状态）
     * @param year
     * @param month
     * @return
     */

    List<MonthSummary>selectSummaryByYearAndMonth(String year,String month);
    /**
     * 通过季度和年份查找所有季度总结（短信已发送和未发送状态）
     * @param year
     * @param month
     * @return
     */

    List<MonthSummary>selectSummaryListByYearAndMonth(String year,String month);


    /**
     * 通过用户code查找当前上一季度得个人总结报告
     * @param year
     * @param month
     * @param employeecode
     * @return
     */
    MonthSummary selectSummaryByYearAndMonthAndCode(String year,String month,String employeecode);

    /**
     * 查询所有的季节总结报告
     * @return
     */
    List<MonthSummary>findMonthSummaryAll();

    /**
     * 批量删除
     * @param serialnos
     * @return
     */
    int batchDelete(List<String>serialnos);


    /**
     * 通过用户编号查找季节编号
     * @param employeecode
     * @return
     */
    List<MonthSummary>selectSerialNoByEmployeeCode(String employeecode);


    /**
     * 批量添加
     * @param list
     * @return
     */
    int batchInsert(List<MonthSummary>list);
    /**
     * 批量修改
     * @param list
     * @return
     */
    int batchUpdate(List<MonthSummary>list);


    List<MonthSummary>selectListByYearAndMonth(String year,String month);

}
