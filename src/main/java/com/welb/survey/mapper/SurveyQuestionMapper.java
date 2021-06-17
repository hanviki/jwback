package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SurveyQuestion question);

    SurveyQuestion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SurveyQuestion question);

    List<SurveyQuestion>selectListByInfoId(Integer surveyinfoid);

    int batchDelete(List<Integer>ids);

    List<SurveyQuestion>getAnswerDetail(Integer surveyinfoid);

    int batchInsert(List<SurveyQuestion> list);

    int deleteQuestionByInfoId(Integer surveyinfoid);


}
