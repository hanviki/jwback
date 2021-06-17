package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.dto.EvaluationClinicalScoreDto;
import com.welb.medicalEthics.entity.EvaluationClinical;
import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.form.SelfSummaryModel;
import com.welb.medicalEthics.service.EvaluationClinicalScoreService;
import com.welb.medicalEthics.service.EvaluationClinicalService;
import com.welb.medicalEthics.service.MedicalEthicsDicService;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.medicalEthics.vo.EvaluationClinicalScoreVo;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description: 临床用户评分
 * @author: luox
 * @date： 2020/11/18 15:07
 */

@Slf4j
@RestController
@RequestMapping("/evaluationClinical")
public class EvaluationClinicalController extends BaseController {

    @Autowired
    private EvaluationClinicalService evaluationClinicalService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private MedicalEthicsUserService service;

    @Autowired
    private IUserService userService;

    @Autowired
    private MedicalEthicsDicService dicService;

    /**
     * 临床用户进入初始化基本信息
     *
     * @param userId 当前用户id
     */
    @RequestMapping("/initInfo/{userId}")
    public Object initInfo(@PathVariable("userId") String userId) {
        ModelMap map = new ModelMap();
        try {
            EvaluationClinical info = evaluationClinicalService.selectByUserId(userId);
            if (null == info) {
                EvaluationClinical initInfo = evaluationClinicalService.initInfo(userId);
                map.put("data", initInfo);
                map.put("msg", "初始化成功");
                map.put("code", 0);
                return ajaxJson(map);
            }
            map.put("data", info);
            map.put("msg", "初始化成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "初始化失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * getBaseInfo 获取用户基础信息
     *
     * @return {@link Object}
     */
    @RequestMapping("/baseInfo")
    public Object getBaseInfo() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> params = Tools.getParamMap(request);
            String userId = params.get("userId");
            EvaluationClinical initInfo = new EvaluationClinical();
            if (StringUtils.isEmpty(userId)) {
                map.put("data", initInfo);
                map.put("msg", "查询失败");
                map.put("code", 1);
            } else {
                initInfo = evaluationClinicalService.selectByUserId(userId);
                if(initInfo != null){
                    initInfo.setPoliticalStatusName(dicService.selectByCodeAndType(DIC_TYPE_POLITICAL_STATUS, initInfo.getPoliticalStatus()));
                    initInfo.setEducationLevelName(dicService.selectByCodeAndType(DIC_TYPE_EDUCATION_LEVEL, initInfo.getEducationLevel()));
                    initInfo.setTitleName(dicService.selectByCodeAndType(DIC_TYPE_TITLE, initInfo.getTitle()));
                }
                map.put("data", initInfo);
                map.put("msg", "基本信息查询成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            map.put("msg", "查询失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * getScoreVo 前端用户评分列表回显
     *
     * @return {@link Object}
     */
    @RequestMapping("/getScore")
    public Object getScoreVo() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> params = Tools.getParamMap(request);
            List<EvaluationClinicalScore> list = scoreService.list(params);
            if (!list.isEmpty()) {
                List<EvaluationClinicalScoreVo> voList = getVoList(list);
                map.put("data", voList);
                map.put("msg", "查询成功");
                map.put("code", 0);
                return ajaxJson(map);
            }
        } catch (Exception e) {
            map.put("msg", "查询失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }


    /**
     * 进入打分表单前为用户生成空白表单
     *
     * @param userId
     * @return
     */
    @RequestMapping("/initFormData/{userId}")
    public Object initFormData(@PathVariable("userId") String userId) {
        ModelMap map = new ModelMap();
        //成功生成了表单条目
        if (scoreService.generateFormItem(userId)) {
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            List<EvaluationClinicalScore> formData = scoreService.getFormData(params);
            if (!formData.isEmpty()) {
                List<EvaluationClinicalScoreVo> voList = getVoList(formData);
                map.put("data", voList);
                map.put("msg", "初始化成功");
                map.put("code", 0);
                return ajaxJson(map);
            }
        }
        map.put("msg", "初始化失败");
        map.put("code", 1);
        return ajaxJson(map);
    }


    /**
     * getVoList 为前端生成表单
     *
     * @param dataList dataList
     * @return {@link List<EvaluationClinicalScoreVo>}
     */
    public List<EvaluationClinicalScoreVo> getVoList(List<EvaluationClinicalScore> dataList) {
        List<EvaluationClinicalScoreVo> voList = new ArrayList<>();
        EvaluationClinicalScoreVo v1 = new EvaluationClinicalScoreVo();
        //1
        v1.setTitle("救死扶伤，全心全意为人民服务(10分)");
        List<EvaluationClinicalScore> v1List = new ArrayList<>();
        dataList.get(0).setQuestion("1、工作认真，负责，细致，责任心强（10分）");
        dataList.get(0).setMaxScore(10);
        v1List.add(dataList.get(0));
        v1.setContent(v1List);
        voList.add(v1);

        //2,3
        EvaluationClinicalScoreVo v2 = new EvaluationClinicalScoreVo();
        v2.setTitle("尊重病人的人格和权利，为病人保守医疗秘密(15分)");
        List<EvaluationClinicalScore> v2List = new ArrayList<>();
        dataList.get(1).setQuestion("1、平等对待患者，做到一视同仁，不得歧视患者(5分)");
        dataList.get(2).setQuestion("2、尊重患者知情权，选择权和隐私权，为患者保守医疗秘密(10分)");
        v2List.add(dataList.get(1));
        dataList.get(1).setMaxScore(5);
        dataList.get(2).setMaxScore(10);
        v2List.add(dataList.get(2));
        v2.setContent(v2List);
        voList.add(v2);

        //4,56
        EvaluationClinicalScoreVo v22 = new EvaluationClinicalScoreVo();
        v22.setTitle("文明礼貌，优质服务，构建和谐医患关系（15分）");
        List<EvaluationClinicalScore> v22List = new ArrayList<>();
        dataList.get(3).setQuestion("1、服务热情周到，态度和蔼可亲,无\"生，冷，硬，顶，退，拖\"现象（5分）");
        dataList.get(4).setQuestion("2、着装整洁，举止端庄，语言文明规范（5分）");
        dataList.get(5).setQuestion("3、认真践行医疗服务承诺，加强与患者的交流沟通，自觉接受监督，构建和谐医患关系（5分）");

        dataList.get(3).setMaxScore(5);
        dataList.get(4).setMaxScore(5);
        dataList.get(5).setMaxScore(5);
        v22List.add(dataList.get(3));
        v22List.add(dataList.get(4));
        v22List.add(dataList.get(5));
        v22.setContent(v22List);
        voList.add(v22);

        //7-8
        EvaluationClinicalScoreVo v3 = new EvaluationClinicalScoreVo();
        v3.setTitle("遵纪守法，廉洁行医(20分)");
        List<EvaluationClinicalScore> v3List = new ArrayList<>();
        dataList.get(6).setQuestion("1、坚持廉洁行医,拒收患者或家属的“红包”等财物,自觉抵制回扣等各种形式商业贿赂,严格执行“九不准”规定（10分)");
        dataList.get(7).setQuestion("2、不开具虚假医学证明,不参与虚假医疗广告宣传和药品医疗器械促销,不隐匿、伪造或违反规定涂改、销毁医学文书及有关资料（10分)");
        dataList.get(6).setMaxScore(10);
        dataList.get(7).setMaxScore(10);
        v3List.add(dataList.get(6));
        v3List.add(dataList.get(7));
        v3.setContent(v3List);
        voList.add(v3);

        //9-10-11
        EvaluationClinicalScoreVo v4 = new EvaluationClinicalScoreVo();
        v4.setTitle("因病施治，规范医疗服务行为(20分)");
        List<EvaluationClinicalScore> v4List = new ArrayList<>();
        dataList.get(8).setQuestion("1、坚持合理检查，合理质量，合理治疗（5分）");
        dataList.get(9).setQuestion("2、认真落实有关控制医疗费用的制度措施（5分）");
        dataList.get(10).setQuestion("3、严格执行医疗服务和药品价格政策,不多收、乱收和私自收取费用（10分）");
        dataList.get(8).setMaxScore(5);
        dataList.get(9).setMaxScore(5);
        dataList.get(10).setMaxScore(10);
        v4List.add(dataList.get(8));
        v4List.add(dataList.get(9));
        v4List.add(dataList.get(10));
        v4.setContent(v4List);
        voList.add(v4);

        //12
        EvaluationClinicalScoreVo v5 = new EvaluationClinicalScoreVo();
        v5.setTitle("顾全大局，团结合作，和谐共事(10分)");
        List<EvaluationClinicalScore> v5List = new ArrayList<>();
        dataList.get(11).setQuestion("1、顾全大局，团结合作，和谐共事(10分)");
        dataList.get(11).setMaxScore(10);
        v5List.add(dataList.get(11));
        v5.setContent(v5List);
        voList.add(v5);

        //13
        EvaluationClinicalScoreVo v6 = new EvaluationClinicalScoreVo();
        v6.setTitle("严谨求实，努力提高专业技术水平(10分)");
        List<EvaluationClinicalScore> v6List = new ArrayList<>();
        dataList.get(12).setQuestion("1、严谨求实，努力提高专业技术水平(10分)");
        dataList.get(12).setMaxScore(10);
        v6List.add(dataList.get(12));
        v6.setContent(v6List);
        voList.add(v6);
        return voList;
    }

    /**
     * 填写自我总结
     */
    @PostMapping("/updateSelfSummary")
    public Object updateSelfSummary(SelfSummaryModel model) {
        ModelMap map = new ModelMap();
        String userId = model.getUserId();
        String selfSummary = model.getSelfSummary();
        if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(selfSummary)) {
            try {
                evaluationClinicalService.updateSelfSummary(userId, selfSummary);
                map.put("msg", "自我总结提交成功");
                map.put("code", 0);
                return ajaxJson(map);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("msg", "自我总结提交失败");
                map.put("code", 1);
            }
        }
        map.put("msg", "错误的请求参数");
        map.put("code", 1);
        return ajaxJson(map);
    }

    /**
     * 填写自我总结
     */
    @PostMapping("/updateSelfSummaryTemp")
    public Object updateSelfSummaryTemp(SelfSummaryModel model) {
        ModelMap map = new ModelMap();
        String userId = model.getUserId();
        String selfSummary = model.getSelfSummary();
        if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(selfSummary)) {
            try {
                evaluationClinicalService.updateSelfSummaryTemp(userId, selfSummary);
                map.put("msg", "自我总结提交成功");
                map.put("code", 0);
                return ajaxJson(map);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("msg", "自我总结提交失败");
                map.put("code", 1);
            }
        }
        map.put("msg", "错误的请求参数");
        map.put("code", 1);
        return ajaxJson(map);
    }

    /**
     * updateBaseInfo 填写基本信息
     *
     * @param evaluationClinical evaluationClinical
     * @return {@link Object}
     */
    @RequestMapping("/updateBaseInfo")
    public Object updateBaseInfo(EvaluationClinical evaluationClinical) {
        ModelMap map = new ModelMap();
        try {
            evaluationClinicalService.updateBaseInfo(evaluationClinical);
            map.put("msg", "基本信息提交成功");
            map.put("code", 0);
            return ajaxJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "基本信息提交失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * submitScore 提交评分表单
     *
     * @param list list
     * @return {@link Object}
     */
    @RequestMapping("/submitScore")
    public Object submitScore(@RequestBody List<EvaluationClinicalScore> list) {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            Integer step = list.get(0).getStep();
            User user = userService.getUserByUserCode(param.get("u_id"));
            List<MedicalEthicsUser> usersList = service.list(new HashMap<String, String>() {{
                put("userId", user.getMoneycard());
            }});
            if (!usersList.isEmpty()) {
                Set<String> roleSet = new HashSet<>();
                usersList.forEach(u -> {
                    String roleCode = u.getRoleCode();
                    //支部书记不参与打分,故不添加支部书记
                    if (!MEDICAL_ETHICS_SECRETARY_USER.equals(roleCode)) {
                        roleSet.add(u.getRoleCode());
                    }
                });
                String roleCode = "";
                //普通用户或不是主任且不是打分书记
                if ((roleSet.size() == 1 && roleSet.contains(MEDICAL_ETHICS_ORDINARY_USER)) || (!roleSet.contains(MEDICAL_ETHICS_ORDINARY_DIRECTOR) && !roleSet.contains(Constant.MEDICAL_ETHICS_SCORING_SECRETARY))) {
                    //普通用户自我评分
                    roleCode = MEDICAL_ETHICS_ORDINARY_USER;
                }

                //主任
                if (roleSet.contains(MEDICAL_ETHICS_ORDINARY_DIRECTOR)) {
                    //普通用户和主任都为一个人
                    if (step == SELF_SUBMIT) {
                        //主任自己提交
                        roleCode = MEDICAL_ETHICS_ORDINARY_USER;
                    }

                    //主任打分
                    if (step == DIRECTOR_SUBMIT) {
                        //主任打分
                        roleCode = MEDICAL_ETHICS_ORDINARY_DIRECTOR;
                    }

                    //普通用户和主任和书记都为一个人
                    if (step == SECRETARY_SUBMIT) {
                        //书记打分
                        roleCode = MEDICAL_ETHICS_SCORING_SECRETARY;
                    }
                }

                //书记
                if (roleSet.contains(Constant.MEDICAL_ETHICS_SCORING_SECRETARY)) {
                    //普通用户和书记都为一个人
                    if (step == SELF_SUBMIT) {
                        //主任自己提交
                        roleCode = MEDICAL_ETHICS_ORDINARY_USER;
                    }

                    //书记当做主任打分
                    if (step == SECRETARY_SUBMIT) {
                        //主任打分
                        roleCode = MEDICAL_ETHICS_ORDINARY_DIRECTOR;
                    }

                    if (step == SECRETARY_SUBMIT) {
                        //普通用户和主任和书记都为一个人
                        roleCode = MEDICAL_ETHICS_SCORING_SECRETARY;
                    }
                }
                //以何种身份进行打分
                scoreService.score(list, roleCode, step, user.getUsername());
            }
            map.put("msg", "评分提交成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "评分提交失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * reScore 重新打分
     *
     * @param dto dto
     * @return {@link Object}
     *
     */
    @RequestMapping("/reScore")
    public Object reScore(@RequestBody EvaluationClinicalScoreDto dto) {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            User user = userService.getUserByUserCode(param.get("u_id"));
            String userName = user.getUsername();
            scoreService.reScore(dto,userName);
            map.put("msg", "评分提交成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "评分提交失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

}
