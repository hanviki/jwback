package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.service.MedicalEvaluationService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description: 医德医风相关
 * @author: luox
 * @date： 2020/12/31
 */

@RequestMapping("/medicalEvaluation")
@RestController
public class MedicalEvaluationController extends BaseController {

    @Autowired
    private MedicalEvaluationService evaluationService;
    /**
     *
     * @return
     */
    @RequestMapping("/batchFinish")
    public Object batchFinish(){
        Map<String, String> param = Tools.getParamMap(request);
        ModelMap map = new ModelMap();
        String userIdListStr = param.get("userIdList");
        if(StringUtils.isNotEmpty(userIdListStr)){
            List<String> userIdList = Arrays.asList(userIdListStr.split(Constant.COMMA));
            try {
                evaluationService.batchFinish(userIdList,Constant.STEP_FINISH);
                map.put("msg", "操作成功");
                map.put("code", 0);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("msg", "操作失败");
                map.put("code", 1);
            }
        }
        map.put("msg", "操作成功");
        map.put("code", 0);
        return ajaxJson(map);
    }

    @RequestMapping("/finishAll")
    public Object finishAll(){
        ModelMap map = new ModelMap();
        try {
            evaluationService.finishAll(Constant.STEP_FINISH);
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

}
