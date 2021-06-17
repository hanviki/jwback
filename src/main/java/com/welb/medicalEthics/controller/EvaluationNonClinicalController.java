package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.entity.EvaluationNonClinical;
import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.service.EvaluationNonClinicalService;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description: 非临床医护人员考评
 * @author: luox
 * @date： 2020/11/17 10:48
 */

@RestController
@RequestMapping("/evaluationNonClinical")
public class EvaluationNonClinicalController extends BaseController {

    @Autowired
    private EvaluationNonClinicalService evaluationNonClinicalService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private IUserService userService;

    Logger logger = LoggerFactory.getLogger(EvaluationNonClinicalController.class);

    /**
     * 根据userId查询考核表单
     *
     * @param userId
     * @return
     */
    @RequestMapping("{userId}")
    public Object selectByUserId(@PathVariable("userId") String userId) {
        ModelMap map = new ModelMap();
        try {
            EvaluationNonClinical evaluationNonClinical = evaluationNonClinicalService.selectByUserId(userId);
            if (null != evaluationNonClinical) {
                map.put("data", evaluationNonClinical);
                map.put("msg", "考核查询成功");
                map.put("code", 0);
            } else {
                logger.error("考核查询异常");
                map.put("msg", "考核查询异常");
                map.put("code", 1);
            }
        } catch (Exception e) {
            logger.error("考核查询异常");
            map.put("msg", "考核查询异常");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * 更新表单信息,打分操作
     *
     * @param evaluationNonClinical
     * @return
     */
    @PostMapping("/updateById")
    public Object update(@RequestBody EvaluationNonClinical evaluationNonClinical) {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            User user = userService.getUserByUserCode(param.get("u_id"));
            Integer result = evaluationNonClinicalService.updateForm(evaluationNonClinical, user.getUsername());
            if (result == 0) {
                map.put("msg", "考核保存成功");
                map.put("code", 0);
            } else if (result == 1) {
                map.put("msg", "考核保存失败");
                map.put("code", 1);
            } else if (result == 2) {
                map.put("msg", "优秀率已超过标准");
                map.put("code", 1);
            }
        } catch (Exception e) {
            map.put("msg", "考核保存失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * editById 打分完成后,重新编辑
     *
     * @param evaluationNonClinical evaluationNonClinical
     * @return {@link Object}
     */
    @PostMapping("/editById")
    public Object editById(@RequestBody EvaluationNonClinical evaluationNonClinical) {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String roleType = evaluationNonClinical.getRoleType();
            if(StringUtils.isNotEmpty(roleType)){
                User user = userService.getUserByUserCode(param.get("u_id"));
                String username = user.getUsername();
                evaluationNonClinicalService.editForm(evaluationNonClinical,username,roleType);
                map.put("msg", "考核保存成功");
                map.put("code", 0);
            }else{
                map.put("msg", "考核保存失败");
                map.put("code", 1);
            }
        } catch (Exception e) {
            map.put("msg", "考核保存失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * 进入表单页面前初始化创建空表单,并获取基础信息
     *
     * @param userId
     * @return
     */
    @PostMapping("/initInfo/{userId}")
    public Object initInfo(@PathVariable("userId") String userId) {
        ModelMap map = new ModelMap();
        try {
            EvaluationNonClinical initInfo = evaluationNonClinicalService.initInfo(userId);
            if (null != initInfo) {
                Integer branchId = initInfo.getBranchId();
                PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(branchId);
                Double currentExcellentPercent = partyBranchRelations.getCurrentExcellentPercent();
                Integer excellentPercent = partyBranchRelations.getExcellentPercent();
                if (currentExcellentPercent == null) {
                    initInfo.setCurrentExcellentPercent("0");
                } else {
                    initInfo.setCurrentExcellentPercent(currentExcellentPercent.toString());
                }

                if (excellentPercent == null) {
                    initInfo.setMaxExcellentPercent("0");
                } else {
                    initInfo.setMaxExcellentPercent(excellentPercent.toString());
                }
                map.put("data", initInfo);
                map.put("msg", "初始化成功");
                map.put("code", 0);
            } else {
                map.put("msg", "初始化失败");
                map.put("code", 1);
            }
        } catch (Exception e) {
            map.put("msg", "初始化失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }


}
