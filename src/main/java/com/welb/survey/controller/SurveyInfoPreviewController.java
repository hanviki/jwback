package com.welb.survey.controller;

import com.alibaba.fastjson.JSONObject;
import com.welb.survey.entity.SurveyInfoPreview;
import com.welb.survey.entity.SurveyOptionPreview;
import com.welb.survey.entity.SurveyQuestionPreview;
import com.welb.survey.info.BatchJson;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.service.ISurveyInfoPreviewService;
import com.welb.survey.service.ISurveyQuestionPreviewService;
import com.welb.util.LogUtil;
import net.sf.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: SurveyPreviewController
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查-预览问卷 控制器
 * @date 2020/7/29
 */
@RestController
@RequestMapping("/preview")
public class SurveyInfoPreviewController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    ISurveyInfoPreviewService previewService;
    @Autowired
    ISurveyQuestionPreviewService questionPreviewService;

    /**
     * 添加预览问卷
     *
     * @param preview
     * @return
     */
    @RequestMapping("/add")
    public Object addSurveyInfo(SurveyInfoPreview preview) {
        ModelMap map = new ModelMap();
        try {
            if (preview.getId() == null) {
                previewService.insertSelective(preview);
            } else {
                previewService.updateByPrimaryKeySelective(preview);
            }
            map.put("msg", "预览问卷成功");
            map.put("id", preview.getId());
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "预览问卷问卷失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 预览问卷
     *
     * @param id
     * @return
     */
    @RequestMapping("/getDetail")
    public Object getDetail(Integer id) {
        ModelMap map = new ModelMap();
        try {
            SurveyInfoPreview detail = previewService.getDetail(id);
            List<BatchJson> batchJsonList = getBatchJsons(detail, id);
            map.put("msg", "查询成功");
            map.put("data", detail);
            map.put("batchJsonList", batchJsonList);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查看失败");
            map.put("code", 1);
        }
        return map;
    }

    private List<BatchJson> getBatchJsons(SurveyInfoPreview detail, Integer id) {
        List<BatchJson> batchJsonList = JSONObject.parseArray(detail.getBatchjson(), BatchJson.class);
        List<SurveyQuestionPreview> questionList = questionPreviewService.selectListByInfoId(id);
        if (batchJsonList != null) {
            List<SurveyOptionPreview> allOptions = questionPreviewService.selectAllOptions();
            for (int i = 0; i < batchJsonList.size(); i++) {
                BatchJson batchJson = batchJsonList.get(i);
                SurveyQuestionPreview question = questionList.get(i);
                batchJson.setQuestionid(question.getId());
                batchJson.setAnswer("");
                batchJson.setIswrite(question.getIswrite());
                batchJson.setQuestionorder(question.getQuestionorder());
                batchJson.setMultiple(question.getMultiple());
                batchJson.setMultipletext(question.getMultipletext());

                List<OptionInfo> optionList = JSONObject.parseArray(batchJson.getNav(), OptionInfo.class);
               if (optionList.size() > 0) {
                    for (int j = 0; j < optionList.size(); j++) {
                        List<SurveyOptionPreview> options = allOptions.stream().filter(o->o.getQuestionid().intValue() == question.getId()).collect(Collectors.toList());
                        if (options.size() > 0) {
                            optionList.get(j).setId(options.get(j).getId());
                        }
                    }
                }
                JSONArray jsonArray = JSONArray.fromObject(optionList);
                batchJson.setNav(jsonArray.toString());
                batchJson.setOptionInfoList(optionList);
            }
        }
        return batchJsonList;
    }
}
