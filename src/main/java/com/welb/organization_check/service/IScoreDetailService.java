package com.welb.organization_check.service;

import com.welb.organization_check.entity.ScoreDetail;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreDetailService
 * @projectName xh-360appraisal-interface
 * @description: 打分详情业务层接口
 * @date 2019/6/1123:44
 */
public interface IScoreDetailService {
    /**
     * 通过dutycode和fserialno查找打分信息
     * @param dutycode
     * @param fserialno
     * @return
     */
    ScoreDetail selectDetailBySerialNo(String dutycode,String fserialno);

    /**
     * 添加打分详情信息
     * @param scoredetail
     * @return
     */
    int insertSelective(ScoreDetail scoredetail);

    /**
     * 修改打分详情信息
     * @param scoredetail
     * @return
     */
    int updateByPrimaryKeySelective(ScoreDetail scoredetail);

    /**
     * 查询各类型的基础得分
     * @param mserialno
     * @param scoretype
     * @param dutytype
     * @return
     */
    Double getTotalScoreByType(String mserialno,String scoretype, String dutytype);
    /**
     * 查询各类型每一道的基础或者关键得分
     * @param mserialno
     * @param scoretype
     * @param dutytype
     * @return
     */
    Double getSingleTotalScoreByType(String mserialno,String scoretype, String dutytype,Integer orderid);
    /**
     * 查询每一道的基础或者关键得分的人数
     * @param mserialno
     * @param scoretype
     * @param dutytype
     * @return
     */
    int getCountByType(String mserialno,String scoretype, String dutytype,Integer orderid);

    /**
     * 查询每一道的基础或者关键得分集合
     * @param mserialno
     * @param scoretype
     * @param dutytype
     * @return
     */
    List<ScoreDetail> getSingleTotalScore(String mserialno,String scoretype,
                                         String dutytype,Integer orderid);


    /**
     * 通过fserialno查找数据
     * @param fserialno
     * @return
     */
    List<ScoreDetail>selectDetailByFSerialNo(String fserialno);


    /**
     * 删除数据
     * @param serialno
     * @return
     */
    int deleteByPrimaryKey(String serialno);


    /**
     * 批量删除
     * @param detailSerialNos
     * @return
     */
    int batchDelete(List<String>detailSerialNos);


    /**
     * 通过FSerialNo查找主键
     * @param fserialno
     * @return
     */
    List<ScoreDetail>selectSerialNoByFSerialNo(String fserialno);

    /**
     * 批量添加
     * @param list
     * @return
     */
    int batchInset(List<ScoreDetail>list);

    /**
     * 批量修改
     * @param list
     * @return
     */
    int batchUpdate(List<ScoreDetail>list);


    /**
     * 批量查找
     * @param list
     * @return
     */
    List<ScoreDetail> batchSelectDetailBySerialNo(List<ScoreDetail> list);

}
