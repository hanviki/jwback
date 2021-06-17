package com.welb.survey.service_impl;

import com.welb.survey.entity.SurveyOptionPreview;
import com.welb.survey.entity.SurveyQuestionPreview;
import com.welb.survey.mapper.SurveyOptionPreviewMapper;
import com.welb.survey.mapper.SurveyQuestionPreviewMapper;
import com.welb.survey.service.ISurveyQuestionPreviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: QuestionServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查-问题接口的实现类
 * @date 2019/12/214:21
 */
@Service
@Transactional
public class SurveyQuestionPreviewServiceImpl implements ISurveyQuestionPreviewService {
    @Resource
    SurveyQuestionPreviewMapper  questionPreviewMapper;
    @Resource
    SurveyOptionPreviewMapper surveyOptionPreviewMapper;

    @Override
    public List<SurveyQuestionPreview> selectListByInfoId(Integer id) {
        return questionPreviewMapper.selectListByInfoId(id);
    }

    @Override
    public List<SurveyOptionPreview> selectOptionsByQuestionId(Integer questionid) {
        return surveyOptionPreviewMapper.selectOptionsByQuestionId(questionid);
    }

    @Override
    public List<SurveyOptionPreview> selectAllOptions() {
        return surveyOptionPreviewMapper.selectAllOptions();
    }

}
