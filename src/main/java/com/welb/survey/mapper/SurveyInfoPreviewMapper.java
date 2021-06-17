package com.welb.survey.mapper;

import com.welb.survey.entity.SurveyInfoPreview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SurveyInfoPreviewMapper {

    int insertSelective(SurveyInfoPreview preview);

    SurveyInfoPreview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SurveyInfoPreview preview);

    SurveyInfoPreview getDetail(Integer id);


}
