package com.welb.survey.service_impl;

import com.alibaba.fastjson.JSONObject;
import com.welb.survey.entity.SurveyInfo;
import com.welb.survey.entity.SurveyOption;
import com.welb.survey.entity.SurveyQuestion;
import com.welb.survey.info.AnswerJson;
import com.welb.survey.entity.Answer;
import com.welb.survey.info.BatchJson;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.mapper.AnswerMapper;
import com.welb.survey.mapper.SurveyInfoMapper;
import com.welb.survey.mapper.SurveyOptionMapper;
import com.welb.survey.mapper.SurveyQuestionMapper;
import com.welb.survey.service.IAnswerService;
import com.welb.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyaozu
 * @title: AnswerServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查 -答案业务层接口的实现类
 * @date 2020/5/816:18
 */
@Service
@Transactional
public class AnswerServiceImpl implements IAnswerService {
    //保留两位小数
    public static DecimalFormat df = new DecimalFormat("0.00");
    @Resource
    AnswerMapper answerMapper;
    @Resource
    SurveyQuestionMapper questionMapper;
    @Resource
    SurveyOptionMapper optionMapper;
    @Resource
    SurveyInfoMapper infoMapper;

    @Override
    public int batchInsert(Integer surveryinfoid, String moneycard, String batchjson) {
        List<Answer> list = new ArrayList<>();
        if (moneycard.equals("") || moneycard == null) {
            moneycard = StringUtil.generatorId();//发薪号为空 则为外院用户 随机生成唯一发薪号
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        List<BatchJson> batchJsonList = JSONObject.parseArray(batchjson, BatchJson.class);
        for (BatchJson json : batchJsonList) {
            if (!json.getAnswer().equals("")) {
                if (json.getType() == 1) {//单选
                    String jsonAnswer = json.getAnswer();
                    String content = "";
                    Integer optionid = Integer.parseInt(jsonAnswer);
                    Answer answer = getAnswer(surveryinfoid, time, json, optionid, moneycard, content);
                    list.add(answer);
                } else if (json.getType() == 2) {//多选
                    String[] answerArr = json.getAnswer().split(",");
                    for (String s : answerArr) {
                        String jsonAnswer = s;
                        String content = "";
                        Integer optionid = Integer.parseInt(jsonAnswer);
                        Answer answer = getAnswer(surveryinfoid, time, json, optionid, moneycard, content);
                        list.add(answer);
                    }
                } else if (json.getType() == 3) {//填空
                    Integer optionid = -1;
                    String content = json.getAnswer();
                    Answer answer = getAnswer(surveryinfoid, time, json, optionid, moneycard, content);
                    list.add(answer);
                }
            }

        }
        if (list.size() > 0) {
            infoMapper.increaseAnswerCount(surveryinfoid);
            return answerMapper.batchInsert(list);
        }
        return 0;
    }

    private Answer getAnswer(Integer surveryinfoid, String time, BatchJson json, Integer optionid, String moneycard, String content) {
        Answer answer = new Answer();
        answer.setOptionid(optionid);
        answer.setQuestionid(json.getQuestionid());
        answer.setSurveyinfoid(surveryinfoid);
        answer.setSubmittime(time);
        answer.setMoneycard(moneycard);
        answer.setContent(content);
        if(json.getType()!=3) {
            if (!json.getNav().equals("")) {
                List<OptionInfo> nav = JSONObject.parseArray(json.getNav(), OptionInfo.class);
                for (OptionInfo info : nav) {
                    if (info.getId().equals(optionid)) {
                        answer.setGaptext(info.getGaptext());
                    }
                }
            } else {
                answer.setGaptext("");
            }
        }

        return answer;
    }

    @Override
    public List<SurveyQuestion> getDetail(Integer surveyinfoid) {
        List<SurveyQuestion> list = new ArrayList<>();
        //通过问卷id查出所有问题集合
        List<SurveyQuestion> questionList = questionMapper.selectListByInfoId(surveyinfoid);
        for (SurveyQuestion question : questionList) {
            //通过问题id查找选项集合
            List<SurveyQuestion> answerDetail = questionMapper.getAnswerDetail(question.getId());
            for (SurveyQuestion surveyQuestion : answerDetail) {
                int questionCount = answerMapper.selectQuestionCount(surveyinfoid, surveyQuestion.getId());
                surveyQuestion.setCount(questionCount);
                List<SurveyOption> surveyOptionList = optionMapper.selectOptionsByQuestionId(surveyQuestion.getId());
                for (SurveyOption option : surveyOptionList) {
                    int count = answerMapper.selectOptionCount(option.getId());
                    option.setCount(count);
                    if (surveyQuestion.getType() != 3) {
                        double ratio = (double) count / questionCount;
                        option.setRatio(df.format(ratio));
                    }
                }
                surveyQuestion.setOptionList(surveyOptionList);
            }
            list.addAll(answerDetail);

        }
        return list;
    }

    @Override
    public List<Answer> getPackList(Answer answer) {
        return answerMapper.getPackList(answer);
    }

    @Override
    public List<Answer> selectAnswerByMoneyCardAndInfoId(Integer surveyinfoid, String moneycard) {
        return answerMapper.selectAnswerByMoneyCardAndInfoId(surveyinfoid, moneycard);
    }

    @Override
    public int batchDelete(List<Integer> ids, Integer surveryinfoid, String moneycard, String batchjson) {
        //同一个人多次提交同一个问卷 ，则删除之前的答案
        answerMapper.batchDelete(ids);
        //添加最新的答案
        int i = batchInsert(surveryinfoid, moneycard, batchjson);
        //因为答案每成功添加一次，答卷数量会加一次 ，由于是同一个人 ，这里就减掉1，保证答题数量不变
        infoMapper.decreaseAnswerCount(surveryinfoid);
        return i;
    }

    @Override
    public List<Answer> getOptionPackList(Answer answer) {
        return answerMapper.getOptionPackList(answer);
    }

}
