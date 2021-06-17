package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.MedicalEthicsMsgTemp;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgMapper;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgTempMapper;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;
import static com.welb.sysBase.util.Constant.USER_TYPE_NO_CLI;

@Service
public class MedicalEthicsMsgTempImpl implements MedicalEthicsMsgTempService {

    @Autowired(required = false)
    private MedicalEthicsMsgTempMapper mapper;

    @Autowired
    private MedicalEthicsMsgMapper msgMapper;

    @Autowired
    private MedicalEthicsUserService  medicalEthicsUserService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    IUserService userService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Override
    public List<MedicalEthicsMsgTemp> list(Map<String, String> params) {
        //按登录用户权限过滤数据
        String uId = params.get("u_id");
        //移除sql默认分页
        params.remove("pageSize");
        params.remove("pageNum");
        Map<String, String> resultMap = medicalEthicsUserService.loadPermissionCondition(uId);
        params.putAll(resultMap);
        List<MedicalEthicsMsgTemp> dataSource = mapper.list(params);
        return dataSource;
    }

    @Override
    public MedicalEthicsMsgTemp selectByUserId(String userId) {
        return mapper.selectByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(int id) {
        //临时表旧数据
        MedicalEthicsMsgTemp medicalEthicsMsgTemp = mapper.list(new HashMap<String, String>(){{put("id", String.valueOf(id));}}).get(0);
        String userId = medicalEthicsMsgTemp.getUserId();
        int personType = medicalEthicsMsgTemp.getPersonType();
        //临床用户删除临床表单
        if(USER_TYPE_CLI == personType){
            clinicalService.deleteByUserId(userId);
            scoreService.deleteByUserId(userId);
        }else if(USER_TYPE_NO_CLI == personType){
            //非临床用户删除非临床表单
            nonClinicalService.deleteByUserId(userId);
        }
        //删除普通用户的角色
        Map<String,String> deleteParam = new HashMap<>();
        deleteParam.put("userId",userId);
        deleteParam.put("roleId","3");
        medicalEthicsUserService.deleteUserRoleByParams(deleteParam);
        //删除医德医风临时表的数据
        mapper.delete(id);
        //删除msg数据
        msgMapper.deleteByUserId(userId);
        Map<String,String> delParam = new HashMap<>();
        delParam.put("userId",userId);
        departmentUserService.deleteByParams(delParam);
    }

    private void handleLoginUser(String userId) {
        User user = userService.selectByMoneyCard(userId);
        if (user != null){
            if (USER_TYPE_MEDICAL_ETHICS.equals(user.getRoletype())){
                //删除医德医风用户信息表数据
                userService.deleteUserByMoneyCard(userId);
            }
            //删除角色的医德医风权限数据
            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUsercode(user.getUsercode());
            userRoleKey.setRolecode(MEDICAL_ETHICS_USER_ROLE);
            userRoleService.deleteByPrimaryKey(userRoleKey);
        }
    }

    @Transactional
    @Override
    public void update(MedicalEthicsMsgTemp medicalEthicsMsgTemp) {
        mapper.update(medicalEthicsMsgTemp);
    }

    @Transactional
    @Override
    public void insert(MedicalEthicsMsgTemp medicalEthicsMsgTemp, ModelMap map) {
        //重复验证
        List<MedicalEthicsMsgTemp> repeatData = mapper.list(new HashMap<>()).stream().filter(m -> m.getUserId().equals(medicalEthicsMsgTemp.getUserId())).collect(Collectors.toList());
        if (repeatData != null && !repeatData.isEmpty()){
            //数据已重复
            MedicalEthicsMsgTemp repeatTemp = repeatData.get(0);
            map.put("msg", "姓名:" + repeatTemp.getUserName() + "[" + repeatTemp.getUserId() + "]" + "系统已存在,无法添加,请联系党支部处理");
            map.put("code", 2);
        }else {
            mapper.insert(medicalEthicsMsgTemp);
            map.put("msg", "数据添加成功");
            map.put("code", 0);
        }
    }

    @Override
    public void insert(MedicalEthicsMsgTemp medicalEthicsMsgTemp) {
        mapper.insert(medicalEthicsMsgTemp);
    }

    @Transactional
    @Override
    public List<String> batchInsert(List<MedicalEthicsMsgTemp> dataSource) {
        List<String> resultList = new ArrayList<>();
        //重复验证
        List<String> userIdsList = dataSource.stream().map(m -> m.getUserId()).collect(Collectors.toList());
        List<MedicalEthicsMsgTemp> repeatData = mapper.list(new HashMap<>()).stream().filter(o -> userIdsList.contains(o.getUserId())).collect(Collectors.toList());
        if (repeatData != null && !repeatData.isEmpty()){
            //数据已重复
            resultList.addAll(repeatData.stream().map(m -> "姓名:" + m.getUserName() + "[" + m.getUserId() + "]在系统中已存在,无法添加,请联系党支部处理").collect(Collectors.toList()));
        }else {
            if (!dataSource.isEmpty()){
                mapper.batchInsert(dataSource);
            }
        }

        return resultList;
    }

    @Override
    public void batchUpdate(List<MedicalEthicsMsgTemp> dataSource) {
        mapper.batchUpdate(dataSource);
    }
}
