package com.welb.sysBase.service.impl;

import com.welb.medicalEthics.dto.ScoreDto;
import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserService;
import com.welb.organization_check.service_impl.UserRoleServiceImpl;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.mapper.BranchDepartmentMapper;
import com.welb.sysBase.mapper.EvaluationDepartmentMapper;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.Constant;
import com.welb.util.DateUtil;
import com.welb.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/3
 */

@Service
public class EvaluationDepartmentServiceImpl implements EvaluationDepartmentService {

    @Autowired
    private EvaluationDepartmentMapper departmentMapper;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private BranchDepartmentMapper branchDepartmentMapper;

    @Autowired
    private HUserService personneService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired
    private MedicalEthicsMsgService medicalEthicsMsgService;

    @Autowired
    MedicalEthicsMsgTempService medicalEthicsMsgTempService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Override
    public EvaluationDepartment selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public List<EvaluationDepartment> list(Map<String, String> params) {
        return departmentMapper.list(params);
    }

    /**
     * 科室主任信息更新
     *
     * @param department
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EvaluationDepartment department) {
        //删除科室-支部关联
        Map<String, String> params = new HashMap<>();
        Integer departmentId = department.getId();
        params.put("departmentId", String.valueOf(department.getId()));
        branchDepartmentMapper.deleteByParams(params);
        //重新写入支部-科室关系
        BranchDepartment branchDepartment = new BranchDepartment();
        branchDepartment.setBranchId(department.getBranchId());
        branchDepartment.setDepartmentId(department.getId());
        branchDepartmentMapper.insert(branchDepartment);
        //更新主任信息
        Integer directorUserId = department.getDirectorUserId();
        //未指定主任
        EvaluationDepartment oldData = departmentMapper.selectById(departmentId);
        //删除原主任所在的科室主任角色
        Map<String, String> deleteParams = new HashMap<>();
        deleteParams.put("roleId", Constant.ROLE_ID_DIRECTOR);
        deleteParams.put("departmentId", String.valueOf(departmentId));
        medicalEthicsUserService.deleteUserRoleByParams(deleteParams);
        if (directorUserId == null) {
            //直接更新基础信息
            departmentMapper.update(department);
        } else {
            //指定了新的主任
            //检查user表
            User user = userService.selectUserByMoneyCard(directorUserId.toString());
            String userCode;
            if (null == user) {
                //新增user,从h_user获取基本信息
                PersonnelUser hUser = personneService.selectByUserId(String.valueOf(directorUserId));
                User newUser = new User();
                newUser.setUsername(hUser.getUsername());
                newUser.setPassword(MD5.md5(INITIAL_PASSWORD));
                newUser.setRoletype(Constant.USER_TYPE_MEDICAL_ETHICS);
                newUser.setUserstate("1");
                newUser.setSex(SEX_MALE.equals(hUser.getSex()) ? "1" : "2");
                newUser.setMoneycard(hUser.getId().toString());
                newUser.setUsercode(directorUserId.toString());
                userCode = directorUserId.toString();
                //插入到user表
                userService.add(newUser);
            } else {
                userCode = user.getUsercode();
            }
            //插入到userRole表
            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUsercode(userCode);
            userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
            List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(userCode);
            //如果没有角色,插入,否则不执行操作
            if (!userRoleKeys.contains(userRoleKey)) {
                userRoleService.insertSelective(userRoleKey);
            }
            //检查考核表是否存在当前用户
            List<MedicalEthicsUser> medicalEthicsUsers = medicalEthicsUserService.selectByUserId(directorUserId.toString());
            if (!medicalEthicsUsers.isEmpty()) {
                MedicalEthicsUser medicalEthicsUser = medicalEthicsUsers.get(0);
                int branchId = medicalEthicsUser.getBranchId();
                //检查考核角色
                List<MedicalEthicsUserRole> roleList = medicalEthicsUserService.selectUserRoleByUserId(directorUserId.toString());
                //新建考核userRole
                MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
                medicalEthicsUserRole.setUserId(directorUserId.toString());
                medicalEthicsUserRole.setUserId(directorUserId.toString());
                medicalEthicsUserRole.setRoleId(Integer.parseInt(Constant.ROLE_ID_DIRECTOR));
                medicalEthicsUserRole.setPartyId(branchId);
                medicalEthicsUserRole.setPartyLevel(Constant.LEVEL4);
                medicalEthicsUserRole.setDepartmentId(departmentId);
                if (roleList.isEmpty()) {
                    medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
                } else {
                    //检查用户在部门是否有对应的角色
                    boolean isSameRole = false;
                    for (MedicalEthicsUserRole userRole : roleList) {
                        if (userRole.getRoleId() == Integer.parseInt(Constant.ROLE_ID_DIRECTOR) && userRole.getDepartmentId().equals(departmentId) && userRole.getUserId().equals(String.valueOf(directorUserId))) {
                            isSameRole = true;
                            break;
                        }
                    }
                    //不存在已有角色,新增否组不处理
                    if (!isSameRole) {
                        medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
                    }
                }
            }
        }
        //更新部门信息
        departmentMapper.update(department);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String departmentId) {
        departmentMapper.delete(departmentId);
        Map<String, String> params = new HashMap<>();
        params.put("departmentId", departmentId);
        branchDepartmentMapper.deleteByParams(params);
        params.put("roleId", Constant.ROLE_ID_DIRECTOR);
        medicalEthicsUserService.deleteUserRoleByParams(params);
    }

    @Override
    public void insert(EvaluationDepartment department) {
        departmentMapper.insert(department);
    }

    /**
     * insertDepartment
     *
     * @param department department
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertDepartment(EvaluationDepartment department) {
        //新增department信息
        BranchDepartment branchDepartment = new BranchDepartment();
        departmentMapper.insert(department);
        //新增科室-支部关联信息
        Integer branchId = department.getBranchId();
        branchDepartment.setBranchId(branchId);
        branchDepartment.setDepartmentId(department.getId());
        //检查是否指定了科室主任
        Integer directorUserId = department.getDirectorUserId();
        //指定了科室主任
        if (directorUserId != null) {
            //检查用户是否可以登录
            User user = userService.selectUserByMoneyCard(directorUserId.toString());
            String userCode;
            if (null == user) {
                //不存在user表,新增
                PersonnelUser hUser = personneService.selectByUserId(String.valueOf(directorUserId));
                User newUser = new User();
                newUser.setUsername(hUser.getUsername());
                newUser.setPassword(MD5.md5(INITIAL_PASSWORD));//初始密码是whuh123456
                newUser.setUserstate("1");
                newUser.setSex(hUser.getSex());
                newUser.setMoneycard(hUser.getId().toString());
                newUser.setUsercode(directorUserId.toString());
                userCode = directorUserId.toString();
                //插入到user表
                userService.add(newUser);
            } else {
                userCode = user.getUsercode();
            }
            //插入到userRole表,赋予医德医风权限
            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUsercode(userCode);
            userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
            List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(userCode);
            //已经存在userRole
            if (!userRoleKeys.contains(userRoleKey)) {
                userRoleService.insertSelective(userRoleKey);
            }

            //检查医德医风用户
            List<MedicalEthicsUser> medicalEthicsUsers = medicalEthicsUserService.selectByUserId(directorUserId.toString());
            if (medicalEthicsUsers.isEmpty()) {

                MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser();
                medicalEthicsUser.setUserId(String.valueOf(directorUserId));
                medicalEthicsUser.setUserName(department.getDirectorName());
                medicalEthicsUser.setBranchId(branchId);
                PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(branchId);
                medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getParentId());
                PartyBranchRelations general = partyBranchRelationsService.selectById(partyBranchRelations.getParentId());
                medicalEthicsUser.setPartyCommitteesId(general.getParentId());
                medicalEthicsUserService.addUser(medicalEthicsUser);
            }
            //添加新的科室主任角色
            MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
            medicalEthicsUserRole.setUserId(directorUserId.toString());
            medicalEthicsUserRole.setRoleId(Integer.parseInt(Constant.ROLE_ID_DIRECTOR));
            medicalEthicsUserRole.setPartyId(branchId);
            medicalEthicsUserRole.setPartyLevel(Constant.LEVEL4);
            medicalEthicsUserRole.setDepartmentId(department.getId());

            List<MedicalEthicsUserRole> roleList = medicalEthicsUserService.selectUserRoleByUserId(directorUserId.toString());
            boolean sameRole = false;
            if (roleList.isEmpty()) {
                //提交新的userRole
                medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
            } else {
                for (MedicalEthicsUserRole userRole : roleList) {
                    if (userRole.getRoleId() == Integer.parseInt(Constant.ROLE_ID_DIRECTOR) && userRole.getUserId().equals(medicalEthicsUserRole.getUserId())
                            && userRole.getPartyId().equals(medicalEthicsUserRole.getPartyId()) && userRole.getDepartmentId().equals(medicalEthicsUserRole.getDepartmentId())) {
                        sameRole = true;
                        break;
                    }
                }
                if (!sameRole) {
                    medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
                }
            }
        }
        branchDepartmentMapper.insert(branchDepartment);
    }

    @Override
    public EvaluationDepartment getDetailById(Integer id) {
        EvaluationDepartment evaluationDepartment = departmentMapper.selectById(id);
        List<String> partyArray = new ArrayList<>();
        List<MedicalEthicsUser> medicalEthicsUsers = medicalEthicsUserService.selectByUserId(evaluationDepartment.getDirectorUserId().toString());
        if (medicalEthicsUsers.size() > 0) {
            evaluationDepartment.setDirectorName(medicalEthicsUsers.get(0).getUserName());
        }
        //支部
        BranchDepartment branchDepartment1 = branchDepartmentMapper.selectByDepartmentId(id);
        PartyBranchRelations p1 = partyBranchRelationsService.selectById(branchDepartment1.getBranchId());
        //总支
        PartyBranchRelations p2 = null;
        if (p1 != null) {
            p2 = partyBranchRelationsService.selectById(p1.getParentId());
        }
        PartyBranchRelations p3;
        //党委
        if (p2 != null) {
            p3 = partyBranchRelationsService.selectById(p2.getParentId());
            partyArray.add(p3.getId().toString());
            partyArray.add(p2.getId().toString());
            partyArray.add(p1.getId().toString());
        }
        evaluationDepartment.setPartyArray(partyArray);
        return evaluationDepartment;
    }


    @Override
    public List<EvaluationDepartment> extendBranchInfo(List<EvaluationDepartment> departmentList) {
        List<EvaluationDepartment> voList = new ArrayList<>();
        for (EvaluationDepartment d : departmentList) {
            Integer branchId = d.getBranchId();
            if (branchId != null) {
                //支部
                PartyBranchRelations partyBranchRelations1 = partyBranchRelationsService.selectById(branchId);
                //总支
                PartyBranchRelations partyBranchRelations2 = partyBranchRelationsService.selectById(partyBranchRelations1.getParentId());
                //党委
                PartyBranchRelations partyBranchRelations3 = partyBranchRelationsService.selectById(partyBranchRelations2.getParentId());
                d.setBranchName(partyBranchRelations1.getRelationsName());
                d.setGeneralBranchName(partyBranchRelations2.getRelationsName());
                if (partyBranchRelations3 != null) {
                    d.setPartyCommitteesName(partyBranchRelations3.getRelationsName());
                } else {
                    d.setPartyCommitteesName("");
                }
                voList.add(d);
            }
        }
        return voList;
    }

    @Override
    public Boolean checkDeleteStatus(Integer id) {
        Map<String, String> params = new HashMap<>();
        params.put("departmentId", String.valueOf(id));
        List<MedicalEthicsUserRole> medicalEthicsUserRoles = medicalEthicsUserService.selectUserRoleByParams(params);
        return medicalEthicsUserRoles.isEmpty();
    }

    /**
     * updateUserInfo 修改基本资料
     *
     * @param departmentUserVo departmentUserVo
     * @return {@link boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(DepartmentUserVo departmentUserVo) {
        String userId = departmentUserVo.getUserId().toString();
        Integer departmentId = departmentUserVo.getDepartmentId();
        if (departmentId == null) {
            return false;
        } else {
            Integer oldDepartmentId;
            List<EvaluationDepartmentUser> evaluationDepartmentUsers = departmentUserService.selectByUserId(departmentUserVo.getUserId());
            if (!evaluationDepartmentUsers.isEmpty()) {
                EvaluationDepartmentUser departmentUser = evaluationDepartmentUsers.get(0);
                oldDepartmentId = departmentUser.getDepartmentId();
                //部门发生了变化
                if (!oldDepartmentId.equals(departmentId)) {
                    Map<String, String> delParams = new HashMap<>();
                    delParams.put("departmentId", oldDepartmentId.toString());
                    delParams.put("userId", userId);
                    //删除后新增
                    departmentUserService.deleteByParams(delParams);
                    EvaluationDepartmentUser newDepartmentUser = new EvaluationDepartmentUser();
                    newDepartmentUser.setUserId(departmentUserVo.getUserId());
                    newDepartmentUser.setDepartmentId(departmentUserVo.getDepartmentId());
                    departmentUserService.insert(newDepartmentUser);

                    BranchDepartment branchDepartment = branchDepartmentMapper.selectByDepartmentId(departmentId);
                    //修改科室id
                    MedicalEthicsMsg medicalEthicsMsg = medicalEthicsMsgService.selectByUserId(userId);
                    MedicalEthicsMsgTemp medicalEthicsMsgTemp = medicalEthicsMsgTempService.selectByUserId(userId);

                    //新的branchId
                    Integer branchId = branchDepartment.getBranchId();
                    //支部
                    PartyBranchRelations p3 = null;
                    //总支
                    PartyBranchRelations p2 = null;
                    //党委
                    PartyBranchRelations p1 = null;
                    //支部
                    p3 = partyBranchRelationsService.selectById(branchId);
                    //
                    if (p3 != null) {
                        p2 = partyBranchRelationsService.selectById(p3.getParentId());
                        medicalEthicsMsg.setBranchId(p3.getId());
                        medicalEthicsMsgTemp.setBranchId(p3.getId());
                    }
                    if (p2 != null) {
                        p1 = partyBranchRelationsService.selectById(p2.getParentId());
                        medicalEthicsMsg.setGeneralBranchId(p2.getId());
                        medicalEthicsMsgTemp.setGeneralBranchId(p2.getId());
                    }
                    if (p1 != null) {
                        medicalEthicsMsg.setPartyCommitteesId(p1.getId());
                        medicalEthicsMsgTemp.setPartyCommitteesId(p1.getId());
                    }
                    //更新已提交msg
                    medicalEthicsMsgService.update(medicalEthicsMsg);
                    //更新未提交msg
                    medicalEthicsMsgTempService.update(medicalEthicsMsgTemp);
                    //更新表单
                    int personType = medicalEthicsMsg.getPersonType();

                    if (Constant.USER_TYPE_CLI == personType) {
                        EvaluationClinical clinical = clinicalService.selectByUserId(userId);
                        if (clinical != null) {
                            if (p3 != null) {
                                clinical.setBranchId(p3.getId());
                            }
                            if (p2 != null) {
                                clinical.setGeneralBranchId(p2.getId());
                            }
                            if (p1 != null) {
                                clinical.setGeneralBranchId(p1.getId());
                            }
                            clinicalService.updateById(clinical);
                        }
                    } else if (Constant.USER_TYPE_NO_CLI == personType) {
                        EvaluationNonClinical nonClinical = nonClinicalService.selectByUserId(userId);
                        if (nonClinical != null) {
                            if (p3 != null) {
                                nonClinical.setBranchId(p3.getId());
                            }
                            if (p2 != null) {
                                nonClinical.setGeneralBranchId(p2.getId());
                            }
                            if (p1 != null) {
                                nonClinical.setGeneralBranchId(p1.getId());
                            }
                            nonClinicalService.updateById(nonClinical);
                        }
                    }
                }
            } else {
                //原部门为空,直接新增
                EvaluationDepartmentUser newDepartmentUser = new EvaluationDepartmentUser();
                newDepartmentUser.setUserId(departmentUserVo.getUserId());
                newDepartmentUser.setDepartmentId(departmentUserVo.getDepartmentId());
                departmentUserService.insert(newDepartmentUser);
            }
            //修改人员类型
            String personType = departmentUserVo.getPersonType();
            MedicalEthicsMsg medicalEthicsMsg = medicalEthicsMsgService.selectByUserId(userId);
            MedicalEthicsMsgTemp msgTemp = medicalEthicsMsgTempService.selectByUserId(userId);
            //原有的personType
            String originPersonType = String.valueOf(medicalEthicsMsg.getPersonType());
            medicalEthicsMsg.setPersonType(Integer.parseInt(personType));
            msgTemp.setPersonType(Integer.parseInt(personType));
            medicalEthicsMsgService.update(medicalEthicsMsg);
            medicalEthicsMsgTempService.update(msgTemp);

            //修改step
            if ("1".equals(originPersonType)) {
                //非临床用户
                EvaluationNonClinical evaluationNonClinical = nonClinicalService.selectByUserId(userId);
                if (evaluationNonClinical != null) {
                    //原有的step
                    Integer originStep = evaluationNonClinical.getStep();
                    Integer newStep = departmentUserVo.getStep();
                    if (!originStep.equals(newStep)) {
                        //修改了状态码,修改了process
                        switch (newStep) {
                            //重置为初始化状态
                            case Constant.NON_CLI_STEP_ZERO:
                                evaluationNonClinical.setStep(Constant.NON_CLI_STEP_ZERO);
                                //重置部门信息
                                evaluationNonClinical.setDeptHeadOpinion(null);
                                evaluationNonClinical.setHeadSubmitTime(null);
                                evaluationNonClinical.setHeadSubmitName(null);
                                //重置书记信息
                                evaluationNonClinical.setBranchOpinion(null);
                                evaluationNonClinical.setBranchSubmitTime(null);
                                evaluationNonClinical.setBranchSubmitName(null);
                                //删除评分
                                evaluationNonClinical.setLevel(null);
                                evaluationNonClinical.setScore(null);
                                break;
                            //重置为完成自我评价
                            case Constant.NON_CLI_STEP_ONE:
                                evaluationNonClinical.setStep(NON_CLI_STEP_ONE);
                                //重置主任信息
                                evaluationNonClinical.setDeptHeadOpinion(null);
                                evaluationNonClinical.setHeadSubmitTime(null);
                                evaluationNonClinical.setHeadSubmitName(null);
                                //重置书记信息
                                evaluationNonClinical.setBranchOpinion(null);
                                evaluationNonClinical.setBranchSubmitTime(null);
                                evaluationNonClinical.setBranchSubmitName(null);
                                //删除评分
                                evaluationNonClinical.setLevel(null);
                                evaluationNonClinical.setScore(null);
                                break;
                            //重置为完成主任评分
                            case Constant.NON_CLI_STEP_TWO:
                                evaluationNonClinical.setStep(NON_CLI_STEP_TWO);
                                //重置书记信息
                                evaluationNonClinical.setBranchOpinion(null);
                                evaluationNonClinical.setBranchSubmitTime(null);
                                evaluationNonClinical.setBranchSubmitName(null);
                                //删除评分
                                evaluationNonClinical.setLevel(null);
                                evaluationNonClinical.setScore(null);
                                break;
                            default:
                                return false;
                        }
                        //更新表单
                        nonClinicalService.updateById(evaluationNonClinical);
                    }
                }
                //非临床用户
            } else if ("0".equals(originPersonType)) {
                EvaluationClinical evaluationClinical = clinicalService.selectByUserId(userId);
                if (evaluationClinical != null) {
                    //原有的step
                    Integer originStep = evaluationClinical.getStep();
                    //新的步骤
                    Integer newStep = departmentUserVo.getStep();
                    //step发生了变化
                    if (!originStep.equals(newStep)) {
                        switch (newStep) {
                            //初始资料未提交
                            case Constant.CLI_STEP_ZERO:
                                //删除评分
                                scoreService.deleteByUserId(userId);

                                evaluationClinical.setStep(newStep);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setBranchOpinion(null);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setScore(null);
                                evaluationClinical.setLevel(null);
                                break;
                            //提交自基本信息
                            case Constant.CLI_STEP_ONE:
                                //填写完自我总结
                            case Constant.CLI_STEP_TWO:
                                evaluationClinical.setStep(newStep);
                                scoreService.deleteByUserId(userId);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setBranchOpinion(null);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setScore(null);
                                evaluationClinical.setLevel(null);
                                break;
                            //填写完自我评分
                            case Constant.CLI_STEP_THREE:
                                evaluationClinical.setStep(newStep);
                                Map<String, String> queryParam = new HashMap<>();
                                queryParam.put("year", CURRENT_YEAR);
                                queryParam.put("userId", userId);
                                List<EvaluationClinicalScore> scoreList = scoreService.list(queryParam);
                                scoreList.forEach(s -> {
                                    //重置科室主任和书记的评分
                                    s.setHeadScore(null);
                                    s.setBranchScore(null);
                                    s.setStep(1);
                                });
                                ScoreDto dto = scoreService.calculateScore(scoreList);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setBranchOpinion(null);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setScore(dto.getScore());
                                evaluationClinical.setLevel(dto.getLevel());
                                //批量更新分数
                                scoreService.batchUpdate(scoreList);
                                break;
                            //重置为主任评分完毕
                            case Constant.CLI_STEP_FOUR:
                                evaluationClinical.setStep(newStep);
                                Map<String, String> queryParamFour = new HashMap<>();
                                queryParamFour.put("year", CURRENT_YEAR);
                                queryParamFour.put("userId", userId);
                                List<EvaluationClinicalScore> scoreListFour = scoreService.list(queryParamFour);
                                scoreListFour.forEach(s -> {
                                    //重置科室主任和书记的评分
                                    s.setBranchScore(null);
                                    s.setStep(2);
                                });
                                ScoreDto dtoFour = scoreService.calculateScore(scoreListFour);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setBranchOpinion(null);
                                evaluationClinical.setDeptHeadOpinion(null);
                                evaluationClinical.setScore(dtoFour.getScore());
                                evaluationClinical.setLevel(dtoFour.getLevel());
                                //批量更新分数
                                scoreService.batchUpdate(scoreListFour);
                                break;
                            default:
                                return false;
                        }
                        clinicalService.updateById(evaluationClinical);
                    }
                }

            }
            return true;
        }
    }

    @Override
    public List<EvaluationDepartment> listAll() {
        return departmentMapper.listAll();
    }

    @Override
    public String getDepartmentNameByUserId(String userId) {
        return departmentMapper.getDepartmentNameByUserId(userId);
    }

    @Override
    public String selectDirectorNameByDepartmentId(int departmentId) {
        return departmentMapper.selectDirectorNameByDepartmentId(departmentId);
    }


}
