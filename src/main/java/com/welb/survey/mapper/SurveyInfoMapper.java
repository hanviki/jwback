package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SurveyInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SurveyInfo surveyInfo);

    SurveyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SurveyInfo surveyInfo);

    List<SurveyInfo> selectSurveyInfoList(SurveyInfo surveyInfo);

    List<SurveyInfo> selectStarSurveyInfoList(SurveyInfo surveyInfo);

    List<SurveyInfo> selectRecycleSurveyInfoList();

    int updateFlag(@Param("flag")Integer flag,@Param("id") Integer id);

    int updatePublishStatus(@Param("publishstatus")Integer publishstatus,@Param("id") Integer id);

    int batchDelete(List<Integer> ids);

    SurveyInfo getDetail(Integer id);

    int increaseAnswerCount(Integer id);

    int decreaseAnswerCount(Integer id);

}
