package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyOptionMapper {

    int batchInsert(List<SurveyOption>option);

    int batchDelete(List<Integer>ids);

    List<SurveyOption>selectOptionsByQuestionId(Integer questionid);

    List<SurveyOption>selectListByInfoId(Integer surveyinfoid);

    int deleteOptionByInfoId(Integer surveyinfoid);

    List<SurveyOption> selectAllOptions();
}
