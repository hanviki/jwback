package com.welb.survey.service;

import com.welb.survey.entity.SurveyInfo;

import java.util.List;

/**
 * @author luoyaozu
 * @title: ISurveyInfoService
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查业务逻辑层接口
 * @date 2019/11/418:02
 */
public interface ISurveyInfoService {
    /**
     * 永久删除单个问卷
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加问卷
     * @param surveyInfo
     * @return
     */
    int insertSelective(SurveyInfo surveyInfo);

    /**
     * 通过id查看问卷
     * @param id
     * @return
     */
    SurveyInfo selectByPrimaryKey(Integer id);

    /**
     * 编辑问卷
     * @param surveyInfo
     * @return
     */
    int updateByPrimaryKeySelective(SurveyInfo surveyInfo);

    /**
     * 查询全部问卷
     * @param surveyInfo
     * @return
     */
    List<SurveyInfo> selectSurveyInfoList(SurveyInfo surveyInfo);

    /**
     * 查询所有星标问卷
     * @param surveyInfo
     * @return
     */
    List<SurveyInfo> selectStarSurveyInfoList(SurveyInfo surveyInfo);

    /**
     * 查询回收站所有问卷
     * @return
     */
    List<SurveyInfo> selectRecycleSurveyInfoList();



    /**
     * 修改删除状态
     * @param id
     * @return
     */
    int updateFlag(Integer id);

    /**
     * 修改发布状态
     * @param publishstatus
     * @param id
     * @return
     */
    int updatePublishStatus(Integer publishstatus,Integer id);


    /**
     * 批量永久删除问卷
     * @param ids
     * @return
     */
    int batchDelete(String ids);

    /**
     *清空回收站
     * @param infoList
     * @return
     */
    int deleteAll(List<SurveyInfo>infoList);

    /**
     * 查看问卷详情/预览问卷
     * @param id
     * @return
     */
    SurveyInfo getDetail(Integer id);

}
