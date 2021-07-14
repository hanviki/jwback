package com.welb.organization_check.service;

import com.welb.organization_check.entity.ScoreFlow;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreFlowService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/516:53
 */
public interface IScoreFlowService {
    /**
     * 查询评分人给被评分人打分的数据
     * @param mserialno
     * @param scorringcode
     * @return
     */
    List<ScoreFlow> selectByScoreFlow(String mserialno,String scorringcode);

    /**
     * 获取各类型分数
     * @param mserialno
     * @param scoretype
     * @return
     */
    Double  getScoreByType(String mserialno,String scoretype);
    /**
     * 获取各类型分数人数
     * @param mserialno
     * @param scoretype
     * @return
     */
    int  getScoreByTypeCount(String mserialno,String scoretype);

    /**
     * 修改各类型分数
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ScoreFlow record);

    /**
     * 添加某类型分数
     * @param record
     * @return
     */
    int insertSelective(ScoreFlow record);

    /**
     * 通过被评人查找上一季度的打分信息
     * @param scoredcode
     * @param year
     * @param month
     * @return
     */
    List<ScoreFlow>selectScoreAllByScoredCode(String scoredcode,String year,String month);

    /**
     * 通过被评人查找最高评分
     * @param scoredcode
     * @param year
     * @param month
     * @return
     */
    Double selectMaxScoreByScoredCode(String scoredcode,String year,String month);

    /**
     * 通过被评人查找最低评分
     * @param scoredcode
     * @param year
     * @param month
     * @return
     */
    Double selectMinScoreByScoredCode(String scoredcode,String year,String month);

    /**
     * 通过被评人code查找所有数据
     * @param scorredcode
     * @return
     */
    List<ScoreFlow> selectFlowByScorredCode(String scorredcode);

    /**
     * 通过评分人code查找所有数据
     * @param scorringcode
     * @return
     */
    List<ScoreFlow> selectFlowByScorringCode(String scorringcode);

    /**
     * 通过被评分人查找上一季度的所有数据
     * @param mserialno
     * @param scoretype
     * @param dutytype
     * @param orderid
     * @return
     */
    List<ScoreFlow> getSingleTotalScoreAll(String mserialno, String scoretype,String dutytype,Integer orderid);

    /**
     * 删除数据
     * @param serialno
     * @return
     */
    int deleteByPrimaryKey(String serialno);


    /**
     * 通过评分人和被评分人code和评分类型查找scoreflow
     * @param scoreFlow
     * @return
     */
    List<ScoreFlow> selectScoreByCodeAndType(ScoreFlow scoreFlow);


    /**
     * 批量删除
     * @param scorringSerialnos
     * @return
     */
    int batchDelete(List<String>scorringSerialnos);


    /**
     * 批量修改
     * @param list
     * @return
     */
    int batchUpdate(List<ScoreFlow>list);


    /**
     * 批量添加
     * @param list
     * @return
     */
    int batchInsert(List<ScoreFlow>list);


    /**
     * 通过季节总结编号和评分人code查找分数
     * @param mserialno
     * @param scorringcode
     * @return
     */
    ScoreFlow selectFlowByMserialNoAndScorringCode(String mserialno,String scorringcode);


    /**
     * 各类型总分/人数=平均分
     * @param mserialno
     * @param scoretype
     * @return
     */
    Double getTypeAvgScore(String mserialno,String scoretype);

    /**
     * 通过季节总结编号和评分人code查找数据总量
     * @param mserialno
     * @param scorringcode
     * @return
     */
    int getTotalCount( String mserialno,String scorringcode);


    List<ScoreFlow>findFlowsByCode(String usercode);

    List<ScoreFlow> selectByScoreFlowType(String mserialno,String scoreType);
}
