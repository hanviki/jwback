package com.welb.sysBase.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.service.BranchDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.StringUtil;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 科室信息管理
 * @author: luox
 * @date： 2020/12/4
 */

@RequestMapping("/evaluationDepartment")
@RestController
public class EvaluationDepartmentController extends BaseController {

    @Autowired
    private EvaluationDepartmentService evaluationDepartmentService;

    @Autowired
    private HUserService personneService;

    @Autowired
    private IUserService userService;

    @Autowired
    private BranchDepartmentService branchDepartmentService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private MedicalEthicsMsgService medicalEthicsMsgService;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private EvaluationDepartmentService departmentService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    /**
     * add 新增部门
     *
     * @param department department
     * @return {@link Object}
     */
    @RequestMapping("/add")
    public Object add(EvaluationDepartment department) {
        ModelMap map = new ModelMap();
        Map<String, String> m = new HashMap<>();
        m.put("departmentName", department.getDepartmentName());
        m.put("branchId", String.valueOf(department.getBranchId()));

        List<EvaluationDepartment> list = evaluationDepartmentService.list(m);
        if (!list.isEmpty()) {
            map.put("msg", "科室名已经存在");
            map.put("code", 1);
            return ajaxJson(map);
        }

        if (StringUtils.isEmpty(department.getDepartmentName())) {
            map.put("msg", "科室名错误");
            map.put("code", 1);
            return ajaxJson(map);
        }

        if (!personneService.checkIdAndName(department.getDirectorUserId(), department.getDirectorName())) {
            map.put("msg", "用户名和发薪号不匹配");
            map.put("code", 1);
            return ajaxJson(map);
        }

        if (department.getBranchId() == null) {
            map.put("msg", "党支部不能为空");
            map.put("code", 1);
        } else {
            PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(department.getBranchId());
            if (3 != partyBranchRelations.getLevel()) {
                map.put("msg", "请指定党支部");
                map.put("code", 1);
            }
        }

        try {
            evaluationDepartmentService.insertDepartment(department);
            map.put("msg", "新增部门成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "新增部门失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    @RequestMapping("/list")
    public Object list() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        int pageNumber = Integer.parseInt(param.get("pageNum"));
        int pageSize = Integer.parseInt(param.get("pageSize"));
        try {
            Page<EvaluationDepartment> page = PageHelper.startPage(pageNumber, pageSize, true);
            List<EvaluationDepartment> list = evaluationDepartmentService.list(param);
            List<EvaluationDepartment> voList = evaluationDepartmentService.extendBranchInfo(list);
            map.put("data", voList);
            map.put("totalPages", page.getTotal());
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/listAll")
    public Object listAll() {
        ModelMap map = new ModelMap();
        try {
            List<EvaluationDepartment> evaluationDepartments = departmentService.listAll();
            map.put("data", evaluationDepartments);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "查询失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    @RequestMapping("/info/{id}")
    public Object info(@PathVariable("id") Integer id) {
        ModelMap map = new ModelMap();
        try {
            EvaluationDepartment evaluationDepartment = evaluationDepartmentService.getDetailById(id);
            map.put("data", evaluationDepartment);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    @RequestMapping("/delete")
    public Object delete() {
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        String id = params.get("id");
        try {
            if (evaluationDepartmentService.checkDeleteStatus(Integer.valueOf(id))) {
                evaluationDepartmentService.delete(id);
                map.put("msg", "删除成功");
                map.put("code", 0);
            } else {
                map.put("msg", "该科室存在关联用户,无法删除");
                map.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "删除失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * update更新科室信息
     *
     * @param department department
     * @return {@link Object}
     */
    @RequestMapping("/update")
    public Object update(EvaluationDepartment department) {
        ModelMap map = new ModelMap();
        if (StringUtils.isEmpty(department.getDepartmentName())) {
            map.put("msg", "科室名错误");
            map.put("code", 1);
            return ajaxJson(map);
        }

        if (!personneService.checkIdAndName(department.getDirectorUserId(), department.getDirectorName())) {
            map.put("msg", "用户名和发薪号不匹配");
            map.put("code", 1);
            return ajaxJson(map);
        }
        if (department.getBranchId() == null) {
            map.put("msg", "党支部不能为空");
            map.put("code", 1);
            return ajaxJson(map);
        } else {
            PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(department.getBranchId());
            if (3 != partyBranchRelations.getLevel()) {
                map.put("msg", "请指定党支部");
                map.put("code", 1);
                return ajaxJson(map);
            }
        }
        try {
            evaluationDepartmentService.update(department);
            map.put("msg", "部门更新成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "部门更新失败");
            map.put("code", 1);
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * getDepartmentEmployees 添加用户到科室
     *
     * @return {@link Object}
     */
    @RequestMapping("/addUser")
    public Object getDepartmentEmployees() {
        Map<String, String> params = Tools.getParamMap(request);
        String userIdList = params.get("userIdList");
        String[] split = userIdList.split(Constant.COMMA);
        List<String> strings = Arrays.asList(split);
        ModelMap map = new ModelMap();
        String userCode = request.getHeader("u_id");
        User user = userService.getUserByUserCode(userCode);
        if (null == user) {
            map.put("code", 1);
            map.put("msg", "用户不存在");
            return ajaxJson(map);
        } else {
            //当前登录用户的id
            String userId = user.getMoneycard();
            //获取当前用户所在的科室
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("directorUserId", userId);
            List<EvaluationDepartment> list = evaluationDepartmentService.list(queryParams);
            if (list.isEmpty()) {
                map.put("code", 1);
                map.put("msg", "未获取到主任信息");
                return ajaxJson(map);
            } else {
                //插入
                EvaluationDepartment evaluationDepartment = list.get(0);
                Integer departmentId = evaluationDepartment.getId();
                List<EvaluationDepartmentUser> dataList = new ArrayList<>();
                for (String id : strings) {
                    EvaluationDepartmentUser data = new EvaluationDepartmentUser();
                    data.setUserId(Integer.parseInt(id));
                    data.setDepartmentId(departmentId);
                    dataList.add(data);
                }
                try {
                    departmentUserService.batchInsert(dataList);
                    map.put("code", 0);
                    map.put("msg", "用户添加成功");
                    return ajaxJson(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            map.put("code", 1);
            map.put("msg", "操作失败");
            return ajaxJson(map);
        }
    }

    @RequestMapping("/removeUser")
    public Object removeDepartmentEmployees(List<String> idList) {
        String userCode = request.getHeader("u_id");
        ModelMap map = new ModelMap();
        User user = userService.getUserByUserCode(userCode);
        if (null == user) {
            map.put("code", 1);
            map.put("msg", "用户不存在");
            return ajaxJson(map);
        } else {
            //当前登录用户的id
            String userId = user.getMoneycard();
            //获取当前用户所在的科室
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("directorUserId", userId);
            List<EvaluationDepartment> list = evaluationDepartmentService.list(queryParams);
            if (list.isEmpty()) {
                map.put("code", 1);
                map.put("msg", "未获取到主任信息");
                return ajaxJson(map);
            } else {
                try {
                    departmentUserService.batchDelete(idList);
                    map.put("code", 0);
                    map.put("msg", "用户添加成功");
                    return ajaxJson(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        map.put("code", 1);
        map.put("msg", "操作失败");
        return ajaxJson(map);
    }

    /**
     * listUser 科室人员管理
     *
     * @return {@link Object}
     */
    @RequestMapping("/listUser")
    public Object listUser() {
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        String userCode = request.getHeader("u_id");
        User user = userService.getUserByUserCode(userCode);
        String userId;
        //分页参数
        Map<String, String> pageParams = new HashMap<String, String>() {{
            put("pageSize", params.get("pageSize"));
            put("pageNum", params.get("pageNum"));
        }};
        if (null == user) {
            map.put("code", 1);
            map.put("msg", "查询失败,用户不存在");
            return ajaxJson(map);
        } else {
            userId = user.getMoneycard();
            Map<String, Object> queryParam = new HashMap<>();
            String username = params.get("username");
            String uid = params.get("userId");
            String personType = params.get("personType");
            String branchId = params.get("branchId");
            String departmentName = params.get("departmentName");

            if (StringUtils.isNotEmpty(username)) {
                queryParam.put("username", username);
            }

            if (StringUtils.isNotEmpty(uid)) {
                queryParam.put("userId", uid);
            }

            if (StringUtils.isNotEmpty(personType)) {
                queryParam.put("personType", personType);
            }

            if (StringUtils.isNotEmpty(branchId)) {
                queryParam.put("branchId", branchId);
            }

            if (StringUtils.isNotEmpty(departmentName)) {
                queryParam.put("departmentName", departmentName);
            }
            //用户所有的支部id
            List<Integer> branchIdList = new ArrayList<>();
            //用户所有的部门id
            List<String> departmentIdList = new ArrayList<>();
            List<MedicalEthicsUserRole> userRoleList = medicalEthicsUserService.selectUserRoleByUserId(userId);
            for (MedicalEthicsUserRole userRole : userRoleList) {
                //超级管理员
                if (1 == userRole.getRoleId()) {
                    //返回所有的数据
                    List<DepartmentUserVo> departmentUserVos = departmentUserService.selectUserDetailByParams(queryParam);
                    List resultList = new ArrayList<>();
                    List<DepartmentUserVo> returnList = new ArrayList<>();

                    if (!departmentUserVos.isEmpty()) {
                        extendStatus(departmentUserVos);
                    }
                    String stepStr =  params.get("step");
                    List<Integer> stepList = new ArrayList<>();
                    if(StringUtils.isNotEmpty(stepStr)){
                        String[] split = stepStr.split(",");
                        List<String> strings = Arrays.asList(split);
                        strings.forEach(s->{
                            if(!stepList.contains(Integer.parseInt(s))){
                                stepList.add(Integer.parseInt(s));
                            }
                        });
                        resultList = departmentUserVos.stream().filter(r->stepList.contains(r.getStep())).collect(Collectors.toList());
                    }else{
                        resultList = departmentUserVos;
                    }
                    returnList = PageUtils.PaginationSettingForPages(pageParams,resultList);
                    returnList = extendDepartmentDirectorName(returnList);
                    map.put("code", 0);
                    map.put("data", returnList);
                    map.put("totalPages", resultList.size());
                    map.put("msg", "查询成功");
                    return ajaxJson(map);
                } else {
                    //支部书记
                    if (2 == (userRole.getRoleId())) {
                        if (!branchIdList.contains(userRole.getPartyId())) {
                            branchIdList.add(userRole.getPartyId());
                        }
                    }
                    //党总支部书记,查询所有的三级支部
                    if (5 == (userRole.getRoleId())) {
                        PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(userRole.getPartyId());
                        Map<String, String> branchParams = new HashMap<>();
                        branchParams.put("parentId", String.valueOf(partyBranchRelations.getId()));
                        List<PartyBranchRelations> relations = partyBranchRelationsService.list(branchParams);
                        for (PartyBranchRelations r : relations) {
                            if (!branchIdList.contains(r.getId())) {
                                branchIdList.add(r.getId());
                            }
                        }
                    }
                    //党委书记
                    if (6 == (userRole.getRoleId())) {
                        PartyBranchRelations commiitteesId = partyBranchRelationsService.selectById(userRole.getPartyId());
                        Map<String, String> q1 = new HashMap<>();
                        q1.put("parentId", commiitteesId.getId().toString());
                        List<PartyBranchRelations> generalBranchList = partyBranchRelationsService.list(q1);
                        for (PartyBranchRelations p : generalBranchList) {
                            q1.put("parentId", String.valueOf(p.getId()));
                            List<PartyBranchRelations> partyList = partyBranchRelationsService.list(q1);
                            partyList.forEach(party -> {
                                if (!branchIdList.contains(party.getId())) {
                                    branchIdList.add(party.getId());
                                }
                            });
                        }
                    }
                    //科室主任
                    if (4 == (userRole.getRoleId())) {
                        Map<String, String> s1 = new HashMap<>();
                        s1.put("directorUserId", userId);
                        List<EvaluationDepartment> departmentList = departmentService.list(s1);
                        departmentList.forEach(e -> {
                            BranchDepartment bd = branchDepartmentService.selectByDepartmentId(e.getId());
                            if (!departmentIdList.contains(bd.getDepartmentId().toString())) {
                                departmentIdList.add(bd.getDepartmentId().toString());
                            }
                        });
                    }
                }
            }
            if (!departmentIdList.isEmpty()) {
                queryParam.put("departmentIdList", departmentIdList);
            }
            if (!branchIdList.isEmpty()) {
                queryParam.put("branchIdList", branchIdList);
            }
            List<DepartmentUserVo> departmentUserVos = departmentUserService.selectUserDetailByParams(queryParam);
            int total = departmentUserVos.size();
            List<DepartmentUserVo> resultList = new ArrayList<>();
            if (!departmentUserVos.isEmpty()) {
                extendStatus(departmentUserVos);
            }
            departmentUserVos = PageUtils.PaginationSettingForPages(pageParams,departmentUserVos);
            String stepStr =  params.get("step");
            List<Integer> stepList = new ArrayList<>();
            if(StringUtils.isNotEmpty(stepStr)){
                String[] split = stepStr.split(",");
                List<String> strings = Arrays.asList(split);
                strings.forEach(s->{
                    if(!stepList.contains(Integer.parseInt(s))){
                        stepList.add(Integer.parseInt(s));
                    }
                });
                resultList = departmentUserVos.stream().filter(r->stepList.contains(r.getStep())).collect(Collectors.toList());
            }else{
                resultList = departmentUserVos;
            }
//            String stepStr = params.get("step");
//            Integer step;
//            if(StringUtils.isNotEmpty(stepStr)){
//                step = Integer.valueOf( params.get("step"));
//                resultList = departmentUserVos.stream().filter(r->r.getStep().equals(step)).collect(Collectors.toList());
//            }else{
//                resultList = departmentUserVos;
//            }

            resultList = extendDepartmentDirectorName(resultList);
            //考核状态过滤
            map.put("code", 0);
            map.put("data", resultList);
            map.put("totalPages",total);
            map.put("msg", "查询成功");
            return ajaxJson(map);
        }
    }

    /**
     * extendStatus 获取科室人员列表的step
     *
     * @param originList originList
     * @return {@link List<DepartmentUserVo>}
     */
    private List<DepartmentUserVo> extendStatus(List<DepartmentUserVo> originList) {
        //所有临床人员
        List<EvaluationClinical> clinicalList = clinicalService.list(new HashMap<>());
        //所有非临床人员
        List<EvaluationNonClinical> nonClinicalList = nonClinicalService.list(new HashMap<>());
        for (DepartmentUserVo departmentUserVo: originList) {
            String userId = String.valueOf(departmentUserVo.getUserId().intValue());
            String personType = departmentUserVo.getPersonType();
            if ("0".equals(personType)) {
                EvaluationClinical existClinical = null;
                for (EvaluationClinical evaluationClinical: clinicalList) {
                    if (evaluationClinical.getUserId().equals(userId)){
                        existClinical = evaluationClinical;
                    }
                }

                if (existClinical != null){
                    int cliStep = existClinical.getStep();
                    departmentUserVo.setStep(cliStep);
                }else {
                    departmentUserVo.setStep(0);
                }
            } else if ("1".equals(personType)) {
                EvaluationNonClinical existNonClinical = null;
                for (EvaluationNonClinical evaluationNonClinical: nonClinicalList) {
                    if (evaluationNonClinical.getUserId().equals(userId)){
                        existNonClinical = evaluationNonClinical;
                    }
                }

                if (existNonClinical != null){
                    int nonStep = existNonClinical.getStep();
                    departmentUserVo.setStep(nonStep);
                }else {
                    departmentUserVo.setStep(0);
                }
            }
        }
        return originList;
    }

    public List<DepartmentUserVo> extendDepartmentDirectorName(List<DepartmentUserVo> originData){
        originData.forEach(o->{
            Integer departmentId = o.getDepartmentId();
            if(departmentId != null){
                o.setDepartmentDirectorName(departmentService.selectDirectorNameByDepartmentId(departmentId));
            }
        });
        return originData;
    }


    /**
     * 科室人员列表基本信息更新
     *
     * @param departmentUserVo
     * @return
     */
    @RequestMapping("/updateBaseInfo")
    public Object userUpdate(DepartmentUserVo departmentUserVo) {
        ModelMap map = new ModelMap();
        try {
            if (departmentService.updateUserInfo(departmentUserVo)) {
                map.put("code", 0);
                map.put("msg", "更新成功");
            } else {
                map.put("code", 1);
                map.put("msg", "更新失败");
            }
        } catch (Exception e) {
            map.put("code", 1);
            map.put("msg", "更新失败");
            e.printStackTrace();
        }
        return ajaxJson(map);
    }

    /**
     * autoDepartment自动为用户分配部门
     *
     * @return {@link Object}
     */
    @RequestMapping("/autoDepartment")
    public Object autoDepartment() {
        //查询所有的已经提交的用户
        List<MedicalEthicsMsg> allUser = medicalEthicsMsgService.selectAll(null);
        ModelMap map = new ModelMap();
        try {
            departmentUserService.autoDepartment(allUser);
            map.put("code", 0);
            map.put("msg", "更新成功");
            return ajaxJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 1);
            map.put("msg", "操作失败");
            return ajaxJson(map);
        }
    }

}
