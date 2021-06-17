package com.welb.survey.service;

import com.welb.survey.entity.Answer;
import com.welb.survey.entity.SurveyQuestion;

import java.util.List;
import java.util.Map;

/**
 * @author luoyaozu
 * @title: IAnswerService
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查 -答案业务层接口
 * @date 2020/5/816:16
 */
public interface IAnswerService {

    /**
     * 批量添加答案
     * @param surveryinfoid
     * @param moneycard
     * @param batchjson
     * @return
     */
    int batchInsert(Integer surveryinfoid,String moneycard,String batchjson);


    /**
     * 查看填空详情
     * @param id
     * @return
     */
    List<SurveyQuestion>  getDetail(Integer id);


    /**
     * 通过问题id查找填空答案集合
     * @param answer
     * @return
     */
    List<Answer>getPackList(Answer answer);

    /**
     * 通过问卷id和发薪号查找问卷答案
     * @param surveyinfoid
     * @param moneycard
     * @return
     */
    List<Answer> selectAnswerByMoneyCardAndInfoId(Integer surveyinfoid,String moneycard);


    /**
     * 批量删除答案
     * @param ids
     * @return
     */
    int batchDelete(List<Integer>ids,Integer surveryinfoid,String moneycard,String batchjson);

    /**
     * 查看选项填空详情
     * @param answer
     * @return
     */
    List<Answer>getOptionPackList(Answer answer);
}
