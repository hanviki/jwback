package com.welb.survey.service_impl;

import com.alibaba.fastjson.JSONObject;
import com.welb.survey.entity.SurveyOption;
import com.welb.survey.entity.SurveyQuestion;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.mapper.SurveyOptionMapper;
import com.welb.survey.mapper.SurveyQuestionMapper;
import com.welb.survey.service.ISurveyQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class SurveyQuestionServiceImpl implements ISurveyQuestionService {
    @Resource
    SurveyQuestionMapper questionMapper;
    @Resource
    SurveyOptionMapper surveyOptionMapper;

    @Override
    public int deleteByPrimaryKey(Integer id, SurveyQuestion question) {
        if (!question.getType().equals("3")) {//如果题目类型不属于填空 即单选或者多选
            //删除选项
            batchDeleteOption(id);
        }
        return questionMapper.deleteByPrimaryKey(id);
    }

    private void batchDeleteOption(Integer id) {
        List<Integer> ids=new ArrayList<>();
        //通过问题id查找选项集合
        List<SurveyOption> list = surveyOptionMapper.selectOptionsByQuestionId(id);
        for (SurveyOption option:list){
            ids.add(option.getId());
        }
        surveyOptionMapper.batchDelete(ids);
    }

    @Override
    public int insertSelective(SurveyQuestion question,String jsonInfo){
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        question.setUpdatetime(time);
        int i = questionMapper.insertSelective(question);
        batchInsertOption(question, jsonInfo);
        return i;
    }

    private void batchInsertOption(SurveyQuestion question, String jsonInfo) {
        if (!jsonInfo.equals("")) {
            //创建题目选项
            List<SurveyOption> option=new ArrayList<>();
            //将json数组转成list集合
            List<OptionInfo> list = JSONObject.parseArray(jsonInfo, OptionInfo.class);
            for (OptionInfo info:list){
                SurveyOption surveyOption = new SurveyOption();
                surveyOption.setOptioncontent(info.getName());
                surveyOption.setQuestionid(question.getId());
                surveyOption.setSurveyinfoid(question.getSurveyinfoid());
                option.add(surveyOption);
            }
             surveyOptionMapper.batchInsert(option);
        }
    }

    @Override
    public SurveyQuestion selectByPrimaryKey(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SurveyQuestion> selectListByInfoId(Integer id) {
        return questionMapper.selectListByInfoId(id);
    }

    @Override
    public List<SurveyOption> selectOptionsByQuestionId(Integer questionid) {
        return surveyOptionMapper.selectOptionsByQuestionId(questionid);
    }

    @Override
    public List<SurveyOption> selectAllOptions() {
        return surveyOptionMapper.selectAllOptions();
    }

}
