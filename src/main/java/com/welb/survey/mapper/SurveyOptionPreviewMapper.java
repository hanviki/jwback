package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyOptionPreview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyOptionPreviewMapper {

    int batchInsert(List<SurveyOptionPreview> option);

    int batchDelete(List<Integer> ids);

    List<SurveyOptionPreview>selectOptionsByQuestionId(Integer questionid);

    List<SurveyOptionPreview>selectListByInfoId(Integer surveyinfoid);

    int deleteOptionByInfoId(Integer surveyinfoid);

    List<SurveyOptionPreview> selectAllOptions();

}
