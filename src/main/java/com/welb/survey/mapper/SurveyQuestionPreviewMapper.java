package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyQuestionPreview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyQuestionPreviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SurveyQuestionPreview question);

    SurveyQuestionPreview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SurveyQuestionPreview question);

    List<SurveyQuestionPreview>selectListByInfoId(Integer surveyinfoid);

    int batchDelete(List<Integer> ids);

    int batchInsert(List<SurveyQuestionPreview> list);

    int deleteQuestionByInfoId(Integer surveyinfoid);

}
