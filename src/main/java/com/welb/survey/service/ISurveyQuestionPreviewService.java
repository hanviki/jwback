package com.welb.survey.service;

import com.welb.survey.entity.SurveyOptionPreview;
import com.welb.survey.entity.SurveyQuestionPreview;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IOuestionService
 * @projectName xh-360appraisal-interface
 * @description: 预览问卷调查-问题接口
 * @date 2019/12/214:20
 */
public interface ISurveyQuestionPreviewService {

    /**
     * 通过
     * @param id
     * @return
     */
    List<SurveyQuestionPreview> selectListByInfoId(Integer id);

    List<SurveyOptionPreview> selectOptionsByQuestionId(Integer questionid);

    List<SurveyOptionPreview> selectAllOptions();
}
