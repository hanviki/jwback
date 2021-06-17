package com.welb.survey.service;

import com.welb.survey.entity.SurveyInfoPreview;

/**
 * @author luoyaozu
 * @title: ISurveyPreviewService
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查-预览问卷接口
 * @date 2020/7/29
 */
public interface ISurveyInfoPreviewService {

    int insertSelective(SurveyInfoPreview preview);

    SurveyInfoPreview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SurveyInfoPreview preview);

    SurveyInfoPreview getDetail(Integer id);

}
