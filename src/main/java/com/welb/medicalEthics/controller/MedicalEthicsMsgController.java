package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgMapper;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.entity.EvaluationDepartment;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.LogUtil;
import com.welb.util.StringUtil;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@RestController
@RequestMapping("/medicalEthicsMsg")
public class MedicalEthicsMsgController extends BaseController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private MedicalEthicsMsgService medicalEthicsMsgService;

    @Autowired
    private IUserService userService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired(required = false)
    private MedicalEthicsMsgMapper mapper;

    @Autowired
    private EvaluationDepartmentService departmentService;

    @RequestMapping("/list")
    public Object list() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<MedicalEthicsMsg> dataSource = medicalEthicsMsgService.list(new HashMap<>(param));
            List<MedicalEthicsMsg> listData = PageUtils.PaginationSettingForPages(param, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", listData);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/resubmitScore")
    public Object resubmitScore() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String personType = param.get("personType");
            String userId = param.get("userId");
            if (EVALUATION_CLINICAL_TEMP_USER.intValue() == Integer.valueOf(personType)) {
                //回退临床
                clinicalService.updateStepByUserId(userId);
            } else if (EVALUATION_CLINICAL_NOT_TEMP_USER.intValue() == Integer.valueOf(personType)) {
                //回退非临床
                nonClinicalService.updateStepByUserId(userId);
            }
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * getCheckList 已考核人员/待考核人员
     *
     * @return {@link Object}
     */
    @RequestMapping("/checkList")
    public Object getCheckList() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        // type 0-待审核   1-已审核
        int type = Integer.parseInt(param.get("type"));
        //评分等级,用于移动端搜索
        String scoreLevelStr = param.get("scoreLevel");
        int scoreLevel = 0;
        if (StringUtils.isNotEmpty(scoreLevelStr)) {
            scoreLevel = Integer.parseInt(param.get("scoreLevel"));
        }

        String userCode = request.getHeader("u_id");
        //多角色需要指定角色类型
        String roleType = param.get("roleType");
        User user = userService.getUserByUserCode(userCode);
        String userId = user.getMoneycard();
        //分页参数
        Map<String, String> pageParams = new HashMap<String, String>() {{
            put("pageSize", param.get("pageSize"));
            put("pageNum", param.get("pageNum"));
        }};
        //查询参数
        Map<String, Object> selectParams = new HashMap<>(param);
        selectParams.remove("pageSize");
        selectParams.remove("pageNum");
        List<MedicalEthicsUserRole> medicalEthicsUserRoles = medicalEthicsUserService.selectUserRoleByUserId(userId);
        List<MedicalEthicsMsg> totalList = new ArrayList<>();
        List returnList = new ArrayList<>();
        if (medicalEthicsUserRoles.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "当前用户没有权限");
        } else {
            String roleId = "";
            Set<String> roleSet = new HashSet<>();
            for (MedicalEthicsUserRole m : medicalEthicsUserRoles) {
                roleSet.add(String.valueOf(m.getRoleId()));
            }
            //当前用户同时拥有支部书记和科室主任的角色，根据前端类型筛选
            if (roleSet.contains(Constant.ROLE_ID_SCORE_DIC) && roleSet.contains(Constant.ROLE_ID_DIRECTOR)) {
                roleId = roleType;
            } else if (roleSet.contains(Constant.ROLE_ID_SCORE_DIC)) {
                roleId = Constant.ROLE_ID_SCORE_DIC;
            } else if (roleSet.contains(Constant.ROLE_ID_DIRECTOR)) {
                roleId = Constant.ROLE_ID_DIRECTOR;
            }
            switch (roleId) {
                //管理员
                case Constant.ROLE_ID_ADMIN:
                    totalList = mapper.listByParam(selectParams).stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                    returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
                    break;
                //打分书记
                case Constant.ROLE_ID_SCORE_DIC:
                    Map<String, String> partyParams = new HashMap<String, String>() {{
                        put("directorUserId", userId);
                    }};
                    List<PartyBranchRelations> list = partyBranchRelationsService.list(partyParams).stream().filter(d -> d.getLevel() == 3).collect(Collectors.toList());
                    List<String> branchIdList = new ArrayList<>();
                    list.forEach(l -> {
                        if (!branchIdList.contains(l.getId().toString())) {
                            branchIdList.add(l.getId().toString());
                        }
                    });
                    Map<String, Object> branchParams = new HashMap<String, Object>() {{
                        put("branchIdList", branchIdList);
                    }};
                    branchParams.putAll(selectParams);
                    List<MedicalEthicsMsg> branchDataRes = mapper.listByParam(branchParams);
                    branchDataRes = extendProcessInfo(branchDataRes);
                    if (type == 0) {
                        totalList = checkProcessStatus(branchDataRes, roleId).stream().filter(d -> d.getIsFinished() == 0).collect(Collectors.toList());
                    } else {
                        totalList = checkProcessStatus(branchDataRes, roleId).stream().filter(d -> d.getIsFinished() == 1).collect(Collectors.toList());
                    }

                    //评分等级过滤
                    if (scoreLevel != 0) {
                        int finalScoreLevel = scoreLevel;
                        totalList = totalList.stream().filter(d -> d.getScoreLevel() == finalScoreLevel).collect(Collectors.toList());
                    }

                    totalList = totalList.stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                    returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
                    break;
                //科室主任
                //获取科室id
                case Constant.ROLE_ID_DIRECTOR:
                    Map<String, String> queryParams = new HashMap<String, String>() {{
                        put("directorUserId", userId);
                    }};
                    List<EvaluationDepartment> departmentList = departmentService.list(queryParams);
                    if (departmentList.isEmpty()) {
                        map.put("msg", "获取书记信息失败");
                        map.put("code", 1);
                    } else {
                        //用户存在多个部门
                        List<String> departmentIdList = new ArrayList<>();
                        departmentList.forEach(d -> {
                            if (!departmentIdList.contains(d.getId().toString())) {
                                departmentIdList.add(d.getId().toString());
                            }
                        });
                        Map<String, Object> deptParams = new HashMap<String, Object>() {{
                            put("departmentIdList", departmentIdList);
                        }};
                        deptParams.putAll(selectParams);
                        List<MedicalEthicsMsg> headDataResList = mapper.listByParam(deptParams);
                        headDataResList = extendProcessInfo(headDataResList);

                        //0-未考核  1-已考核
                        if (type == 0) {
                            totalList = checkProcessStatus(headDataResList, roleId).stream().filter(h -> h.getIsFinished() == 0).collect(Collectors.toList());
                        } else if (type == 1) {
                            totalList = checkProcessStatus(headDataResList, roleId).stream().filter(h -> h.getIsFinished() == 1).collect(Collectors.toList());
                        }

                        //评分等级过滤
                        if (scoreLevel != 0) {
                            int finalScoreLevel = scoreLevel;
                            totalList = totalList.stream().filter(d -> d.getScoreLevel() == finalScoreLevel).collect(Collectors.toList());
                        }

                        totalList = totalList.stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                        returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                        map.put("totalPages", totalList.size());
                        map.put("data", returnList);
                        map.put("code", 0);
                    }
                    break;
                default:
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
            }
        }
        return ajaxJson(map);
    }

    /**
     * 检查审核进度
     *
     * @param originList
     * @param roleId
     * @return
     */
    private List<MedicalEthicsMsg> checkProcessStatus(List<MedicalEthicsMsg> originList, String roleId) {
        originList.forEach(data -> {
            int personType = data.getPersonType();
            String userId = data.getUserId();
            //临床人员状态检查
            if (Constant.USER_TYPE_CLI == personType) {
                EvaluationClinical evaluationClinical = clinicalService.selectByUserId(userId);
                if (null != evaluationClinical) {
                    if (evaluationClinical.getLevel() != null) {
                        data.setScoreLevel(evaluationClinical.getLevel());
                    }
                    Integer step = evaluationClinical.getStep();
                    //打分书记
                    if (Constant.ROLE_ID_SCORE_DIC.equals(roleId)) {
                        //临床状态为4,可以操作
                        if (CLI_STEP_FOUR == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }

                        if (Constant.CLI_STEP_FIVE == step) {
                            data.setIsFinished(1);
                        } else {
                            data.setIsFinished(0);
                        }
                        //科室主任
                    } else if (Constant.ROLE_ID_DIRECTOR.equals(roleId)) {
                        if (step < CLI_STEP_FOUR) {
                            data.setIsFinished(0);
                        } else {
                            data.setIsFinished(1);
                        }
                        //步骤为3科室主任可以操作
                        if (CLI_STEP_THREE == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                    }
                }
                //非临床用户
            } else if (Constant.USER_TYPE_NO_CLI == personType) {
                //非临床
                EvaluationNonClinical evaluationNonClinical = nonClinicalService.selectByUserId(userId);
                if (evaluationNonClinical != null) {
                    if (evaluationNonClinical.getLevel() != null) {
                        data.setScoreLevel(evaluationNonClinical.getLevel());
                    }
                    Integer step = evaluationNonClinical.getStep();
                    //主任
                    if (Constant.ROLE_ID_DIRECTOR.equals(roleId)) {
                        if (step >= Constant.NON_CLI_STEP_TWO) {
                            data.setIsFinished(1);
                        } else {
                            data.setIsFinished(0);
                        }
                        if (NON_CLI_STEP_ONE == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                        //支部书记
                    } else if (Constant.ROLE_ID_SCORE_DIC.equals(roleId)) {
                        if (step <= Constant.NON_CLI_STEP_TWO) {
                            data.setIsFinished(0);
                        } else {
                            data.setIsFinished(1);
                        }
                        if (NON_CLI_STEP_TWO == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                    }
                }
            }
        });
        return originList;
    }

    /**
     * doSubmit 临时用户提交
     *
     * @return {@link Object}
     */
    @RequestMapping("/doSubmit")
    public Object doSubmit() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            //查询临时表数据
            List<Integer> idList = Arrays.stream(param.get("ids").split(COMMA)).map(r -> Integer.valueOf(r)).collect(Collectors.toList());
            List<String> resultList = medicalEthicsMsgService.doSubmit(idList);
            map.put("msg", resultList);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * getMyList 我的考核列表
     *
     * @param request request
     * @param year    当前年份
     * @return {@link Object} 对应的列表
     */
    @RequestMapping("/getMyList")
    public Object getMyList(HttpServletRequest request, String year) {
        String userCode = request.getHeader("u_id");
        ModelMap map = new ModelMap();
        User user = userService.getUserByUserCode(userCode);
        if (null != user) {
            String userId = user.getMoneycard();
            MedicalEthicsMsg medicalEthicsMsg = medicalEthicsMsgService.selectByUserId(userId);
            if (null == medicalEthicsMsg) {
                //无考核用户
                map.put("code", 0);
                map.put("msg", "未获取到考核用户");
                map.put("personType", 999);
                map.put("data", new ArrayList<>());
            } else {
                Integer personType = medicalEthicsMsg.getPersonType();
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                if (StringUtil.isNotEmpty(year)) {
                    params.put("year", year);
                }
                //返回临床用户列表
                if (Constant.USER_TYPE_CLI.intValue() == personType.intValue()) {
                    params.put("userId", userId);
                    List<EvaluationClinical> resultList = clinicalService.list(params);
                    if (!resultList.isEmpty()) {
                        resultList = clinicalService.extendPartyInfo(resultList);
                        String departmentName = departmentService.getDepartmentNameByUserId(userId);
                        resultList.forEach(d -> {
                            d.setPersonType(personType);
                            d.setDepartmentName(departmentName);
                        });
                    }
                    map.put("personType", personType);
                    map.put("totalCount", resultList.size());
                    map.put("data", resultList);
                    map.put("code", 0);

                } else if (Constant.USER_TYPE_NO_CLI.intValue() == personType.intValue()) {
                    //返回非临床用户列表
                    List<EvaluationNonClinical> resultList = nonClinicalService.list(params);
                    if (!resultList.isEmpty()) {
                        resultList = nonClinicalService.extendPartyInfo(resultList);
                        String departmentName = departmentService.getDepartmentNameByUserId(userId);
                        resultList.forEach(d -> {
                            d.setPersonType(personType);
                            d.setDepartmentName(departmentName);
                        });
                    }
                    //处理分数
                    map.put("personType", personType);
                    map.put("data", resultList);
                    map.put("totalCount", resultList.size());
                    map.put("code", 0);
                }
            }
        }
        return ajaxJson(map);
    }

    /**
     * 删除已经提交的用户
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String userId = param.get("userId");
            if (StringUtils.isEmpty(userId)) {
                map.put("msg", "未查询到用户信息");
                map.put("code", 1);
            } else {
                medicalEthicsMsgService.delete(userId);
                map.put("msg", "操作成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * extendProcessInfo 根据personType获取step信息
     *
     * @param dataList dataList
     * @return {@link List<MedicalEthicsMsg>}
     */
    private List<MedicalEthicsMsg> extendProcessInfo(List<MedicalEthicsMsg> dataList) {
        if (dataList != null) {
            dataList.forEach(o -> {
                int personType = o.getPersonType();
                if (Constant.USER_TYPE_CLI == personType) {
                    //临床人员
                    EvaluationClinical evaluationClinical = clinicalService.selectByUserId(o.getUserId());
                    if (evaluationClinical != null) {
                        o.setStep(evaluationClinical.getStep());
                    }
                    //非临床用户
                } else if (Constant.USER_TYPE_NO_CLI == personType) {
                    EvaluationNonClinical evaluationNonClinical = nonClinicalService.selectByUserId(o.getUserId());
                    if (evaluationNonClinical != null) {
                        o.setStep(evaluationNonClinical.getStep());
                    }
                }
            });
        }
        return dataList;
    }
}
