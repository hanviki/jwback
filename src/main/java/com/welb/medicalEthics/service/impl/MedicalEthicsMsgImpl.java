package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgMapper;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgTempMapper;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.service.BranchDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.IPermissionService;
import com.welb.sysBase.util.Constant;
import com.welb.util.MD5;
import com.welb.util.PageData;
import com.welb.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@Service
public class MedicalEthicsMsgImpl implements MedicalEthicsMsgService {

    @Autowired(required = false)
    private MedicalEthicsMsgMapper mapper;

    @Autowired(required = false)
    private MedicalEthicsMsgTempMapper medicalEthicsMsgTempMapper;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired
    private BranchDepartmentService branchDepartmentService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;


    @Override
    public List<MedicalEthicsMsg> list(Map<String, String> params) {
        //按登录用户权限过滤数据
        String uId = params.get("u_id");
        //移除sql默认分页
        params.remove("pageSize");
        params.remove("pageNum");
        Map<String, String> resultMap = medicalEthicsUserService.loadPermissionCondition(uId);
        params.putAll(resultMap);
        //查询数据
        List<MedicalEthicsMsg> dataSource = mapper.list(params);
        //处理返回人员类型
//        dataSource.stream().forEach(m -> m.setPersonType(this.loadPersonType(m)));
        //按人员类型查询
        if (!StringUtil.isEmpty(params.get("personType"))) {
            dataSource = dataSource.stream().filter(d -> d.getPersonType() == Integer.valueOf(params.get("personType"))).collect(Collectors.toList());
        }
        //按登录用户权限过滤数据
        return dataSource;
    }

    @Override
    public List<MedicalEthicsMsg> getList(Map<String, String> params) {
        return mapper.list(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String userId) {
        MedicalEthicsMsg medicalEthicsMsg = selectByUserId(userId);
        int personType = medicalEthicsMsg.getPersonType();
        //临床用户删除临床表单
        if (USER_TYPE_CLI == personType) {
            clinicalService.deleteByUserId(userId);
            scoreService.deleteByUserId(userId);
        } else if (USER_TYPE_NO_CLI == personType) {
            //非临床用户删除非临床表单
            nonClinicalService.deleteByUserId(userId);
        }
        //删除msg表
        mapper.deleteByUserId(userId);
        //删除考核表的用户角色为普通用户的user-role
        Map<String, String> deleteParams = new HashMap<>();
        deleteParams.put("roleId", "3");
        deleteParams.put("userId", userId);
        medicalEthicsUserService.deleteUserRoleByParams(deleteParams);
        //删除临时表的数据
        medicalEthicsMsgTempMapper.deleteByUserId(userId);
        Map<String, String> delParam = new HashMap<>();
        delParam.put("userId", userId);
        departmentUserService.deleteByParams(delParam);
    }

    @Override
    public void deleteByUserId(String userId) {
        mapper.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void update(MedicalEthicsMsg medicalEthicsMsg) {
        mapper.update(medicalEthicsMsg);
    }

    @Transactional
    @Override
    public List<String> insert(MedicalEthicsMsg medicalEthicsMsg) {
        List<String> resultList = new ArrayList<>();
        //重复验证
        List<MedicalEthicsMsg> repeatData = mapper.list(new HashMap<>()).stream().filter(m -> m.getUserId().equals(medicalEthicsMsg.getUserId())).collect(Collectors.toList());
        if (repeatData != null && !repeatData.isEmpty()) {
            //数据已重复
            resultList.add("数据已存在,无法添加");
        } else {
            mapper.insert(medicalEthicsMsg);
            resultList.add("数据添加成功");
        }

        return resultList;
    }

    @Transactional
    @Override
    public List<String> batchInsert(List<MedicalEthicsMsg> dataSource) {
        List<String> resultList = new ArrayList<>();
        return resultList;
    }

    @Transactional
    @Override
    public List<String> doSubmit(List<Integer> idList) {
        List<String> resultList = new ArrayList<>();
        //更新临时表数据的状态
        List<MedicalEthicsMsgTemp> tempList = medicalEthicsMsgTempMapper.listByIds(idList);
        List<MedicalEthicsMsg> medicalEthicsMsgList = new ArrayList<>();
        List<MedicalEthicsUser> medicalEthicsUserList = new ArrayList<>();
        //提交用户
        List<String> userIdList = tempList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        List<PageData> submitUser = permissionService.showUserByUserId(userIdList);
        //提交的登录权限用户
        List<PageData> submitLoginUser = permissionService.showLoginUserByUserId(userIdList);
        List<UserRoleKey> insertUserRoleList = new ArrayList<>();
        tempList.stream().forEach(o -> {
            //真实表
            MedicalEthicsMsg medicalEthicsMsg = new MedicalEthicsMsg(o.getUserId(), o.getPartyCommitteesId(), o.getGeneralBranchId(), o.getBranchId(), o.getUserName(), STATUS_ZERO, o.getPersonType());
            medicalEthicsMsgList.add(medicalEthicsMsg);
            //当前用户
            PageData curUser = submitUser.stream().filter(s -> o.getUserId().equals(s.getString("u_id"))).collect(Collectors.toList()).get(0);
            //性别
            int sex = curUser.getString("u_sex").equals(SEX_MALE) ? STATUS_ONE : STATUS_ZERO;
            //身份证
            String idCard = curUser.getString("u_id_card");
            //来院时间
            String hireData = curUser.getString("u_hospital_time");
            //用户信息表
            MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser(o.getUserId(),
                    o.getUserName(),
                    sex,
                    o.getPartyCommitteesId(),
                    o.getGeneralBranchId(),
                    o.getBranchId(),
                    idCard,
                    null,
                    null,
                    null,
                    hireData,
                    null,
                    STATUS_ZERO,
                    null, o.getPersonType());
            medicalEthicsUserList.add(medicalEthicsUser);
            //登录相关
            this.handleLoginUser(curUser, submitLoginUser, insertUserRoleList);
        });

        if (!idList.isEmpty()) {
            //更新临时人员信息表(更新status为已提交)
            tempList.stream().forEach(o -> o.setStatus(STATUS_ONE));
            medicalEthicsMsgTempMapper.batchUpdate(tempList);
        }

        //批量插入用户角色权限数据
        if (!insertUserRoleList.isEmpty()) {
            userRoleService.batchInsert(insertUserRoleList);
        }

        //医德医风相关表数据重复验证
        this.handleRepeatData(medicalEthicsMsgList, medicalEthicsUserList, resultList);
        return resultList;
    }

    @Override
    public MedicalEthicsMsg selectByUserId(String userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public MedicalEthicsMsg selectById(Integer id) {
        return mapper.selectByUId(id);
    }

    @Override
    public List<MedicalEthicsMsg> selectByBranchId(Integer branchId) {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", String.valueOf(branchId));
        return list(params);
    }

    @Override
    public List<MedicalEthicsMsg> selectAll(Map<String, String> params) {
        return mapper.selectAll(params);
    }

    @Override
    public List<String> allIdList() {
        return mapper.allIdList();
    }

    @Override
    public List<MedicalEthicsMsg> showMissingUsers() {
        return mapper.showMissingUsers();
    }

    /**
     * selectCommonUser 查询所有的普通用户
     *
     * @return {@link List<MedicalEthicsMsg>}
     */
    @Override
    public List<MedicalEthicsMsg> selectCommonUser(String branchId) {
        return mapper.selectCommonUser(branchId);
    }

    /**
     * updateMsg 更新已提交人员
     *
     * @param userId       userId
     * @param departmentId departmentId
     * @param personType   personType
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMsg(String userId, String departmentId, String personType) {
        MedicalEthicsMsgTemp medicalEthicsMsgTemp = medicalEthicsMsgTempMapper.selectByUserId(userId);
        //更新人员类型
        medicalEthicsMsgTemp.setPersonType(Integer.parseInt(personType));
        //科室人员绑定
        EvaluationDepartmentUser departmentUser = new EvaluationDepartmentUser();
        departmentUser.setUserId(Integer.parseInt(userId));
        departmentUser.setDepartmentId(Integer.parseInt(departmentId));
        departmentUserService.bindNewUser(departmentUser);
        //根据科室获取支部信息
        BranchDepartment branchDepartment = branchDepartmentService.selectByDepartmentId(Integer.parseInt(departmentId));
        Integer branchId = branchDepartment.getBranchId();
        int generalBranchId = 0;
        int committeedId = 0;
        PartyBranchRelations branchParty = partyBranchRelationsService.selectById(branchId);
        if (branchParty != null) {
            PartyBranchRelations generalParty = partyBranchRelationsService.selectById(branchParty.getParentId());
            if (generalParty != null) {
                generalBranchId = generalParty.getId();
                PartyBranchRelations committeedParty = partyBranchRelationsService.selectById(generalParty.getParentId());
                if (committeedParty != null) {
                    committeedId = committeedParty.getId();
                    //更新temp的支部信息
                    medicalEthicsMsgTemp.setBranchId(branchId);
                    medicalEthicsMsgTemp.setGeneralBranchId(generalBranchId);
                    medicalEthicsMsgTemp.setPartyCommitteesId(committeedId);
                }
            }
        }

        //处理message
        MedicalEthicsMsg medicalEthicsMsg = mapper.selectByUserId(userId);
        if (medicalEthicsMsg != null) {
            medicalEthicsMsg.setPersonType(Integer.parseInt(personType));
            medicalEthicsMsg.setBranchId(branchId);
            medicalEthicsMsg.setGeneralBranchId(generalBranchId);
            medicalEthicsMsg.setPartyCommitteesId(committeedId);
            mapper.update(medicalEthicsMsg);
        }

        medicalEthicsMsgTempMapper.update(medicalEthicsMsgTemp);
        //临床删除非临床表单
        if (Integer.parseInt(personType) == USER_TYPE_CLI) {
            EvaluationClinical clinical = clinicalService.selectByUserId(userId);
            nonClinicalService.deleteByUserId(userId);
            if (clinical != null) {
                clinical.setBranchId(branchId);
                clinical.setGeneralBranchId(generalBranchId);
                clinical.setPartyCommitteesId(committeedId);
                clinicalService.updateById(clinical);
            }
        } else if (Integer.parseInt(personType) == USER_TYPE_NO_CLI) {
            EvaluationNonClinical nonClinical = nonClinicalService.selectByUserId(userId);
            //删除临床用户表单
            clinicalService.deleteByUserId(userId);
            scoreService.deleteByUserId(userId);
            if (nonClinical != null) {
                nonClinical.setBranchId(branchId);
                nonClinical.setGeneralBranchId(generalBranchId);
                nonClinical.setPartyCommitteesId(committeedId);
                nonClinicalService.updateById(nonClinical);
            }
        }
    }

    private void handleLoginUser(PageData curUser, List<PageData> submitLoginUser, List<UserRoleKey> insertUserRoleList) {
        //检查用户表中是否已有此用户
        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setRolecode(MEDICAL_ETHICS_USER_ROLE);
        String userCode;
        if (!submitLoginUser.isEmpty()) {
            //用户表已存在此用户,只需添加对应的用户的医德医风权限数据即可
            PageData curLoginUser = submitLoginUser.stream().filter(s -> s.getString("MoneyCard").equals(curUser.getString("u_id"))).collect(Collectors.toList()).get(0);
            userCode = curLoginUser.getString("UserCode");
            //增加医德医风权限
            userRoleKey.setUsercode(userCode);
        } else {
            //用户表无此用户,需要添加新用户及其医德医风权限数据
            User user = new User();
            //检查邮箱是否合法
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            String email = curUser.getString("u_email");
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                user.setEmail(email);
            } else {
                user.setEmail("");
            }
            user.setUsername(curUser.getString("u_name"));
            user.setPassword(MD5.md5(INITIAL_PASSWORD));
            user.setMobile(curUser.getString("u_phone"));
            user.setNation(curUser.getString("u_nation"));
            user.setMoneycard(curUser.getString("u_id"));
            user.setSex(SEX_MALE.equals(curUser.getString("u_sex")) ? "1" : "2");
            //默认给为问卷调查类型(0-组织部  1-人事部  2-调查问卷' 3-医德医风)
            user.setRoletype(USER_TYPE_MEDICAL_ETHICS);
            userService.insertSelective(user);
            userCode = user.getUsercode();
            //增加新用户的医德医风权限
            userRoleKey.setUsercode(userCode);
        }

        //查询此人是否有医德医风权限
        UserRoleKey repeatData = userRoleService.selectOne(userRoleKey);
        if (repeatData == null) {
            insertUserRoleList.add(userRoleKey);
        }
    }

    private void handleRepeatData(List<MedicalEthicsMsg> medicalEthicsMsgList, List<MedicalEthicsUser> medicalEthicsUserList, List<String> resultList) {
        //新增数据到真实表
        List<String> userIdsList = medicalEthicsMsgList.stream().map(m -> m.getUserId()).collect(Collectors.toList());
        //真实表重复数据
        List<MedicalEthicsMsg> repeatMsgData = mapper.listByUserId(userIdsList);
        //用户信息表重复数据
        List<MedicalEthicsUser> repeatUserData = medicalEthicsUserService.listByUserId(userIdsList);
        if (repeatMsgData != null && !repeatMsgData.isEmpty()) {
            //若数据已存在真实表,则删除已存在的数据
            mapper.batchDelete(repeatMsgData.stream().map(m -> m.getId()).collect(Collectors.toList()));
        }
        if (!medicalEthicsMsgList.isEmpty()) {
            //添加最新的数据到真实表
            mapper.batchInsert(medicalEthicsMsgList);
        }

        if (repeatUserData != null && !repeatUserData.isEmpty()) {
            //若数据已存在用户信息表,则删除已存在的数据
            medicalEthicsUserService.batchDelete(repeatUserData.stream().map(m -> m.getId()).collect(Collectors.toList()));
            //若数据已存在用户角色表,则删除已存在的数据
//            medicalEthicsUserService.batchDeleteUserRole(repeatUserData.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
            medicalEthicsUserService.batchDeleteCommonUserRole(repeatUserData.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
        }

        if (!medicalEthicsUserList.isEmpty()) {
            //添加数据到用户信息表
            medicalEthicsUserService.batchInsert(medicalEthicsUserList);
            //添加到用户角色表
            List<MedicalEthicsUserRole> userRoleList = new ArrayList<>();
            medicalEthicsUserList.stream().forEach(m -> {
                MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
                medicalEthicsUserRole.setUserId(m.getUserId());
                medicalEthicsUserRole.setRoleId(medicalEthicsUserService.getRoleInfo(new HashMap<String, String>() {{
                    put("roleCode", MEDICAL_ETHICS_ORDINARY_USER);
                }}).getId());
                userRoleList.add(medicalEthicsUserRole);
            });
            if (!userRoleList.isEmpty()) {
                medicalEthicsUserService.batchInsertIntoUserRole(userRoleList);
            }
            resultList.add("数据添加成功");
        }
    }

    /**
     * 获取人员类型
     *
     * @return
     */
    private int loadPersonType(MedicalEthicsMsg medicalEthicsMsg) {
        int personType;
        //(党委/党总支/党支部)
        if (medicalEthicsMsg.getBranchId() != NUMBER_ZERO) {
            //存在党支部关系,取党支部人员类型
            personType = medicalEthicsMsg.getBranchPersonType();
        } else if (medicalEthicsMsg.getBranchId() == NUMBER_ZERO && medicalEthicsMsg.getGeneralBranchId() != NUMBER_ZERO) {
            //不存在党支部关系,存在党总支关系,则取党总支人员类型
            personType = medicalEthicsMsg.getGeneralBranchPersonType();
        } else {
            //只存在党委关系,则取党委人员类型
            personType = medicalEthicsMsg.getPartyCommitteesPersonType();
        }

        return personType;
    }
}
