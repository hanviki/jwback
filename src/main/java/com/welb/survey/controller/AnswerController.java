package com.welb.survey.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.survey.entity.Answer;
import com.welb.survey.entity.SurveyInfo;
import com.welb.survey.entity.SurveyQuestion;
import com.welb.survey.service.IAnswerService;
import com.welb.survey.service.ISurveyInfoService;
import com.welb.survey.service.ISurveyQuestionService;
import com.welb.sysBase.service.HUserService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: AnswerController
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查 -答案控制器
 * @date 2020/5/816:23
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    IAnswerService answerService;
    @Autowired
    ISurveyInfoService surveyInfoService;
    @Autowired
    ISurveyQuestionService questionService;
    @Autowired
    HUserService personneService;

    /**
     * 批量添加答案
     *
     * @param surveryinfoid
     * @param batchjson
     * @return
     */
    @RequestMapping("/batchInsert")
    public Object batchInsert(Integer surveryinfoid, String batchjson, String moneycard) {
        ModelMap map = new ModelMap();
        try {
                List<Integer> ids = new ArrayList<>();
                SurveyInfo info = surveyInfoService.selectByPrimaryKey(surveryinfoid);
                if (info.getPublishstatus() == 2) {
                    List<Answer> answers = answerService.selectAnswerByMoneyCardAndInfoId(surveryinfoid, moneycard);
                    if (answers.size() == 0) {
                        answerService.batchInsert(surveryinfoid, moneycard, batchjson);
                        map.put("msg", "提交成功");
                        map.put("code", 0);
                    } else {
                        for (Answer answer : answers) {
                            ids.add(answer.getId());
                        }
                        if (ids.size() > 0) {
                            answerService.batchDelete(ids, surveryinfoid, moneycard, batchjson);
                            map.put("msg", "提交成功");
                            map.put("code", 0);
                        } else {
                            map.put("msg", "提交失败");
                            map.put("code", 1);
                        }
                    }
                } else {
                    map.put("msg", "该问卷还未发布");
                    map.put("code", 1);
                }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "提交失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 答卷分析
     *
     * @param surveyinfoid
     * @return
     */
    @RequestMapping("/getAnswerDetail")
    public Object getDetail(Integer surveyinfoid) {
        ModelMap map = new ModelMap();
        try {
            SurveyInfo info = surveyInfoService.selectByPrimaryKey(surveyinfoid);
            //该问卷答卷总人数
            int answerCount = info.getAnswercount();

            if (answerCount > 0) {
                List<SurveyQuestion> list = answerService.getDetail(surveyinfoid);
                map.put("msg", "查看详情成功");
                map.put("data", list);
                map.put("title", info.getTitle());
                map.put("code", 0);
            } else {
                if (info.getPublishstatus() == 2) {
                    map.put("msg", "此问卷暂时还没有答卷，请先回收答卷");
                    map.put("code", 1);
                } else {
                    map.put("msg", "该问卷还未发布,请先发布问卷。");
                    map.put("code", 1);
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查看详情失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过问题id查看填空列表
     *
     * @param pageNum
     * @param pageSize
     * @param answer
     * @return
     */

    @RequestMapping("/getPackList")
    public Object getPackList(int pageNum, int pageSize, Answer answer) {
        ModelMap map = new ModelMap();
        try {
            SurveyQuestion question = questionService.selectByPrimaryKey(answer.getQuestionid());
            PageHelper.startPage(pageNum, pageSize);
            List<Answer> list = answerService.getPackList(answer);
            PageInfo<Answer> pageInfo = new PageInfo<>(list);
            map.put("msg", "查看成功");
            map.put("totalPages", pageInfo.getTotal());
            map.put("title", question.getQuestiontitle());
            map.put("data", list);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查看失败");
            map.put("code", 1);
        }
        return map;

    }


    /**
     * 通过问题id查看填空列表
     *
     * @param pageNum
     * @param pageSize
     * @param answer
     * @return
     */

    @RequestMapping("/getOptionPackList")
    public Object getOptionPackList(int pageNum, int pageSize, Answer answer) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Answer> list = answerService.getOptionPackList(answer);
            PageInfo<Answer> pageInfo = new PageInfo<>(list);
            map.put("msg", "查看成功");
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", list);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查看失败");
            map.put("code", 1);
        }
        return map;

    }
}
