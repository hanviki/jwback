package com.welb.survey.service;

import com.welb.survey.entity.SurveyOption;
import com.welb.survey.entity.SurveyQuestion;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IOuestionService
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查-问题接口
 * @date 2019/12/214:20
 */
public interface ISurveyQuestionService {

    /**
     * 删除问题
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id, SurveyQuestion question);

    /**
     * 添加单个问题
     * @param question
     * @return
     */
    int insertSelective(SurveyQuestion question,String jsonInfo);

    /**
     * 通过id查询问题
     * @param id
     * @return
     */
    SurveyQuestion selectByPrimaryKey(Integer id);


    List<SurveyQuestion> selectListByInfoId(Integer id);

    List<SurveyOption> selectOptionsByQuestionId(Integer questionid);

    List<SurveyOption> selectAllOptions();
}
