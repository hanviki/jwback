package com.welb.survey.service_impl;

import com.alibaba.fastjson.JSONObject;
import com.welb.survey.entity.SurveyInfoPreview;
import com.welb.survey.entity.SurveyOptionPreview;
import com.welb.survey.entity.SurveyQuestionPreview;
import com.welb.survey.info.BatchJson;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.mapper.SurveyInfoPreviewMapper;
import com.welb.survey.mapper.SurveyOptionPreviewMapper;
import com.welb.survey.mapper.SurveyQuestionPreviewMapper;
import com.welb.survey.service.ISurveyInfoPreviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: SurveyPreviewServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查-预览问卷 接口的实现类
 * @date 2020/7/29
 */
@Service
@Transactional
public class SurveyInfoPreviewServiceImpl implements ISurveyInfoPreviewService {
    @Resource
    SurveyInfoPreviewMapper previewMapper;
    @Resource
    SurveyQuestionPreviewMapper questionPreviewMapper;
    @Resource
    SurveyOptionPreviewMapper optionPreviewMapper;

    @Override
    public int insertSelective(SurveyInfoPreview preview) {
        String batchJson = preview.getBatchjson();
        int i = previewMapper.insertSelective(preview);
        //添加题目/题目选项
        addQuestionsAndOptions(preview, batchJson);
        return i;
    }

    private void addQuestionsAndOptions(SurveyInfoPreview preview, String batchJson) {
        List<BatchJson> batchJsonList = JSONObject.parseArray(batchJson, BatchJson.class);
        int questionorder = 0;
        List<SurveyOptionPreview> optionPreviewList = new ArrayList<>();
        for (int i = 0; i < batchJsonList.size(); i++) {
            SurveyQuestionPreview question = new SurveyQuestionPreview();
            BatchJson json = batchJsonList.get(i);
            if (json.getType() != 4) {
                questionorder += 1;
            } else {

            }
            //添加题目
            addQuestion(preview.getId(), json, question, questionorder);
            //添加问题选项
            batchInsertOption(preview.getId(), json, question,optionPreviewList);
        }
        if(optionPreviewList.size()>0){
            optionPreviewMapper.batchInsert(optionPreviewList);
        }
    }

    private void addQuestion(Integer surveyinfoid, BatchJson json, SurveyQuestionPreview question, int questionorder) {
        question.setSurveyinfoid(surveyinfoid);
        question.setType(json.getType());
        question.setQuestiontitle(json.getTitle());
        question.setIswrite(json.getIswrite());
        question.setMultiple(json.getMultiple());
        question.setMultipletext(json.getMultipletext());
        if (json.getType() == 4) {
            question.setQuestionorder(0);
        } else {
            question.setQuestionorder(questionorder);
        }
        questionPreviewMapper.insertSelective(question);

    }

    private void batchInsertOption(Integer surveyinfoid, BatchJson json, SurveyQuestionPreview question,List<SurveyOptionPreview> optionPreviewList) {
        List<OptionInfo> list = JSONObject.parseArray(json.getNav(), OptionInfo.class);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals("")) {
                SurveyOptionPreview surveyOption = new SurveyOptionPreview();
                surveyOption.setOptioncontent(list.get(i).getName());
                surveyOption.setQuestionid(question.getId());
                surveyOption.setSurveyinfoid(surveyinfoid);
                surveyOption.setOptionorder(i);
                surveyOption.setOptionorder(i);
                surveyOption.setGap(list.get(i).getGap());
                surveyOption.setGaptext(list.get(i).getGaptext());
                optionPreviewList.add(surveyOption);
            }
        }

    }

    @Override
    public SurveyInfoPreview selectByPrimaryKey(Integer id) {
        return previewMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SurveyInfoPreview preview) {
        String batchJson = preview.getBatchjson();
        if (!batchJson.equals("")) {
            SurveyInfoPreview infoPreview = previewMapper.selectByPrimaryKey(preview.getId());
            if (!infoPreview.getBatchjson().equals(preview.getBatchjson())) {
                questionPreviewMapper.deleteQuestionByInfoId(preview.getId());
                optionPreviewMapper.deleteOptionByInfoId(preview.getId());
                //添加题目/题目选项
                addQuestionsAndOptions(preview, batchJson);
            }
        }
        return previewMapper.updateByPrimaryKeySelective(preview);
    }

    @Override
    public SurveyInfoPreview getDetail(Integer id) {
        return previewMapper.getDetail(id);
    }

}
