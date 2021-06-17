package com.welb.survey.service_impl;

import com.alibaba.fastjson.JSONObject;
import com.welb.survey.entity.Answer;
import com.welb.survey.entity.SurveyInfo;
import com.welb.survey.entity.SurveyOption;
import com.welb.survey.entity.SurveyQuestion;
import com.welb.survey.info.BatchJson;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.mapper.AnswerMapper;
import com.welb.survey.mapper.SurveyInfoMapper;
import com.welb.survey.mapper.SurveyOptionMapper;
import com.welb.survey.mapper.SurveyQuestionMapper;
import com.welb.survey.service.ISurveyInfoService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author luoyaozu
 * @title: SurveyInfoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查接口的实现类
 * @date 2019/11/418:03
 */
@Service
@Transactional
public class SurveyInfoServiceImpl implements ISurveyInfoService {
    @Resource
    SurveyInfoMapper surveyInfoMapper;
    @Resource
    AnswerMapper answerMapper;
    @Resource
    SurveyQuestionMapper questionMapper;
    @Resource
    SurveyOptionMapper optionMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        List<Integer> questionIds = new ArrayList<>();
        List<Integer> answerIds = new ArrayList<>();
        List<Integer> optionIds = new ArrayList<>();
        //删除与问卷id关联的数据
        deleteAboutInfoIdData(questionIds, answerIds, optionIds, id);
        return surveyInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(SurveyInfo surveyInfo) {
        surveyInfo.setAnswercount(0);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        surveyInfo.setCreatetime(time);
        String batchJson = surveyInfo.getBatchjson();
        int i = surveyInfoMapper.insertSelective(surveyInfo);
        //添加题目/题目选项
        addQuestionsAndOptions(surveyInfo, batchJson, time);

        return i;
    }

    private void addQuestionsAndOptions(SurveyInfo surveyInfo, String batchJson, String time) {
        List<BatchJson> batchJsonList = JSONObject.parseArray(batchJson, BatchJson.class);
        int questionorder = 0;
        List<SurveyOption> optionList = new ArrayList<>();
        for (int i = 0; i < batchJsonList.size(); i++) {
            SurveyQuestion question = new SurveyQuestion();
            BatchJson json = batchJsonList.get(i);
            if (json.getType() != 4) {
                questionorder += 1;
            }else {

            }
            //添加题目
            addQuestion(surveyInfo.getId(), time, json, question, questionorder);
            //添加问题选项
            batchInsertOption(surveyInfo.getId(), json, question,optionList);
        }

        if (optionList.size()>0){
            optionMapper.batchInsert(optionList);
        }
    }

    private void addQuestion(Integer surveyinfoid, String time, BatchJson json, SurveyQuestion question, int questionorder) {
        question.setSurveyinfoid(surveyinfoid);
        question.setType(json.getType());
        question.setQuestiontitle(json.getTitle());
        question.setUpdatetime(time);
        question.setIswrite(json.getIswrite());
        question.setMultiple(json.getMultiple());
        question.setMultipletext(json.getMultipletext());
        if (json.getType()==4){
            question.setQuestionorder(0);
        }else {
            question.setQuestionorder(questionorder);
        }

       questionMapper.insertSelective(question);
    }

    private void batchInsertOption(Integer surveyinfoid, BatchJson json, SurveyQuestion question,List<SurveyOption> optionList) {
        List<OptionInfo> list = JSONObject.parseArray(json.getNav(), OptionInfo.class);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getName().equals("")) {
                SurveyOption surveyOption = new SurveyOption();
                surveyOption.setOptioncontent(list.get(i).getName());
                surveyOption.setQuestionid(question.getId());
                surveyOption.setSurveyinfoid(surveyinfoid);
                surveyOption.setOptionorder(i);
                surveyOption.setGap(list.get(i).getGap());
                surveyOption.setGaptext(list.get(i).getGaptext());
                optionList.add(surveyOption);
            }
        }
    }

    @Override
    public SurveyInfo selectByPrimaryKey(Integer id) {
        return surveyInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SurveyInfo surveyInfo) {
        surveyInfo.setPublishstatus(1);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        surveyInfo.setUpdatetime(time);
        String batchJson = surveyInfo.getBatchjson();
        if (!batchJson.equals("")) {
            List<Integer> questionIds = new ArrayList<>();
            List<Integer> answerIds = new ArrayList<>();
            List<Integer> optionIds = new ArrayList<>();
            //删除与问卷id关联的数据
            deleteAboutInfoIdData(questionIds, answerIds, optionIds, surveyInfo.getId());
            //添加题目/题目选项
            addQuestionsAndOptions(surveyInfo, batchJson, time);
        }
        return surveyInfoMapper.updateByPrimaryKeySelective(surveyInfo);
    }

    @Override
    public List<SurveyInfo> selectSurveyInfoList(SurveyInfo surveyInfo) {
        List<SurveyInfo> list = surveyInfoMapper.selectSurveyInfoList(surveyInfo);
        for (SurveyInfo info : list) {
            int count = answerMapper.getAnswerCount(info.getId());
            info.setAnswercount(count);
        }
        return list;
    }

    @Override
    public List<SurveyInfo> selectStarSurveyInfoList(SurveyInfo surveyInfo) {
        List<SurveyInfo> list = surveyInfoMapper.selectStarSurveyInfoList(surveyInfo);
        for (SurveyInfo info : list) {
            int count = answerMapper.getAnswerCount(info.getId());
            info.setAnswercount(count);
        }
        return list;
    }

    @Override
    public List<SurveyInfo> selectRecycleSurveyInfoList() {
        return surveyInfoMapper.selectRecycleSurveyInfoList();
    }

    @Override
    public int updateFlag(Integer id) {
        SurveyInfo surveyInfo = surveyInfoMapper.selectByPrimaryKey(id);
        Integer flag = null;
        if (surveyInfo.getFlag() == 1) {
            flag = 2;
        } else if (surveyInfo.getFlag() == 2) {
            flag = 1;
        }
        return surveyInfoMapper.updateFlag(flag, id);
    }

    @Override
    public int updatePublishStatus(Integer publishstatus, Integer id) {
        SurveyInfo surveyInfo = surveyInfoMapper.selectByPrimaryKey(id);
        if (surveyInfo.getPublishstatus() == 1) {
            publishstatus = 2;
        } else if (surveyInfo.getPublishstatus() == 2) {
            publishstatus = 1;
        }
        return surveyInfoMapper.updatePublishStatus(publishstatus, id);
    }

    @Override
    public int batchDelete(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<Integer> idsList = new ArrayList<>();
        List<Integer> questionIds = new ArrayList<>();
        List<Integer> answerIds = new ArrayList<>();
        List<Integer> optionIds = new ArrayList<>();
        for (String idStr : list) {
            int id = Integer.parseInt(idStr);
            idsList.add(id);
            //删除与问卷id关联的数据
            deleteAboutInfoIdData(questionIds, answerIds, optionIds, id);
        }

        return surveyInfoMapper.batchDelete(idsList);
    }

    @Override
    public int deleteAll(List<SurveyInfo> infoList) {
        List<Integer> infoIds = new ArrayList<>();
        List<Integer> questionIds = new ArrayList<>();
        List<Integer> answerIds = new ArrayList<>();
        List<Integer> optionIds = new ArrayList<>();
        for (SurveyInfo info : infoList) {
            Integer id = info.getId();
            infoIds.add(id);
            //删除与问卷id关联的数据
            deleteAboutInfoIdData(questionIds, answerIds, optionIds, id);
        }

        return surveyInfoMapper.batchDelete(infoIds);
    }

    private void deleteAboutInfoIdData(List<Integer> questionIds, List<Integer> answerIds, List<Integer> optionIds, Integer id) {
       /* List<SurveyQuestion> questionList = questionMapper.selectListByInfoId(id);
        for (SurveyQuestion question : questionList) {
            questionIds.add(question.getId());
        }*/
        List<Answer> answerList = answerMapper.selectListByInfoId(id);
        for (Answer answer : answerList) {
            answerIds.add(answer.getId());
        }
        /*List<SurveyOption> optionList = optionMapper.selectListByInfoId(id);
        for (SurveyOption option : optionList) {
            optionIds.add(option.getId());
        }
        if (questionIds.size() > 0) {//批量删除问题
            questionMapper.batchDelete(questionIds);
        }*/
        if (answerIds.size() > 0) {//批量删除答案
            answerMapper.batchDelete(answerIds);
        }
       /* if (optionIds.size() > 0) {//批量删除选项
            optionMapper.batchDelete(optionIds);
        }*/
        //批量删除问题
        questionMapper.deleteQuestionByInfoId(id);
        //批量删除选项
        optionMapper.deleteOptionByInfoId(id);

    }


    @Override
    public SurveyInfo getDetail(Integer id) {

        return surveyInfoMapper.getDetail(id);
    }


}
