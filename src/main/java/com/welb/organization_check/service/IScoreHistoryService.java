package com.welb.organization_check.service;

import com.welb.organization_check.entity.ScoreHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreHistoryService
 * @projectName xh-360appraisal-interface
 * @description: 历史评分数据接口
 * @date 2020/7/21
 */

public interface IScoreHistoryService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScoreHistory history);

    ScoreHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreHistory history);

    /**
     * 查询某个用户的历史评分数据
     * @param history
     * @return
     */
    ScoreHistory selectOneByHistory(ScoreHistory history);

    /**
     * 查询所有除打分用户外的所有用户历史数据
     * @param history
     * @return
     */
    List<ScoreHistory> selectHistoryList(ScoreHistory history);

    /**
     * 查询所有打分用户的历史数据
     * @param history
     * @return
     */
    List<ScoreHistory>gradeList(ScoreHistory history);

    /**
     * 查询所有打分用户的当前季度评分数据
     * @param history
     * @return
     */
    List<ScoreHistory>selectGradeHisotyList(ScoreHistory history);

    /**
     * 查询所有打分用户的当前季度评分数据
     * @param history
     * @return
     */
    List<ScoreHistory>selectUserHisotyList(ScoreHistory history);


    /**
     * 批量添加评分数据
     * @param list
     * @return
     */
    int batchInsert(List<ScoreHistory>list);


    /**
     * 通过用户编号查找当前季度的评分数据
     * @param usercode
     * @return
     */
    ScoreHistory selectScoreHistoryByUserCode(String usercode);


    /**
     * 一键导出未评分和未完成数据
     * @param year
     * @param month
     * @return
     */
    List<ScoreHistory>oneClickDown(@Param("year")String year, @Param("month")String month);
    /**
     * 查询当前季度所有用户评分数据
     * @param year
     * @param month
     * @return
     */
    List<ScoreHistory>findScoreHistoryList(@Param("year")String year, @Param("month")String month);



}
