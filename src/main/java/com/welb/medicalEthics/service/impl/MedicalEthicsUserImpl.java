package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.MedicalEthicsRoleInfo;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import com.welb.medicalEthics.mapper.MedicalEthicsUserMapper;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.mapper.UserMapper;
import com.welb.sysBase.service.HUserService;
import com.welb.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@Service
public class MedicalEthicsUserImpl implements MedicalEthicsUserService {

    @Autowired(required = false)
    private MedicalEthicsUserMapper mapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private HUserService personneService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Override
    public List<MedicalEthicsUser> selectByUserId(String userId) {
        return mapper.selectByUserId(userId);
    }

    /**
     * 更新人员基本信息
     *
     * @param medicalEthicsUser
     */
    @Override
    public void updateBaseInfo(MedicalEthicsUser medicalEthicsUser) {
        mapper.updateBaseInfo(medicalEthicsUser);
    }

    @Override
    public List<MedicalEthicsUser> list(Map<String, String> params) {
        List<MedicalEthicsUser> dataSource = null;
        try {
            dataSource = mapper.list(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //处理返回人员类型
        dataSource.stream().forEach(m -> m.setPersonType(loadPersonType(m)));
        return dataSource;
    }

    @Override
    public List<MedicalEthicsUser> listRoles(Map<String, String> params) {
        String uId = params.get("u_id");
        Map<String, String> permissionMap = this.loadPermissionCondition(uId);
        List<MedicalEthicsUser> dataSource = mapper.list(permissionMap);
        //处理返回人员类型
        dataSource.stream().forEach(m -> m.setPersonType(loadPersonType(m)));
        //按人员类型查询
        if (!StringUtil.isEmpty(params.get("personType"))) {
            dataSource = dataSource.stream().filter(d -> d.getPersonType() == Integer.valueOf(params.get("personType"))).collect(Collectors.toList());
        }
        //按发薪号查询
        if (!StringUtil.isEmpty(params.get("userId"))) {
            dataSource = dataSource.stream().filter(d -> d.getUserId().equals(params.get("userId"))).collect(Collectors.toList());
        }
        //按姓名查询
        if (!StringUtil.isEmpty(params.get("userName"))) {
            dataSource = dataSource.stream().filter(d -> d.getUserName().contains(params.get("userName"))).collect(Collectors.toList());
        }
        //按党支部关系查询
        if (!StringUtil.isEmpty(params.get("relationsId"))) {
            int relationsId = Integer.valueOf(params.get("relationsId"));
            dataSource = dataSource.stream().filter(d -> d.getPartyCommitteesId() == relationsId || d.getGeneralBranchId() == relationsId || d.getBranchId() == relationsId).collect(Collectors.toList());
        }
        return dataSource;
    }

    @Override
    public List<MedicalEthicsUserRole> showRoleMsg(Map<String, String> params) {
        return mapper.showRoleMsg(params);
    }

    @Override
    public String getHighestPermission(String uId) {
        String roleCode;
        User user = userMapper.getUserByUserCode(uId);
        List<MedicalEthicsUserRole> roleList = mapper.showRoleMsg(new HashMap<String, String>() {{
            put("userId", user.getMoneycard());
        }});
        if (roleList != null && !roleList.isEmpty()) {
            roleCode = roleList.stream().map(m -> Integer.valueOf(m.getRoleCode())).min(Comparator.naturalOrder()).orElse(null).toString();
        } else {
            roleCode = STRING_ZERO;
        }
        return roleCode;
    }

    @Override
    public Map<String, String> loadPermissionCondition(String uId) {
        Map<String, String> returnMap = new HashMap<>();
        User user = userMapper.getUserByUserCode(uId);
        String highestPermission = this.getHighestPermission(uId);
        if (MEDICAL_ETHICS_ADMINISTRATOR.equals(highestPermission)) {
            //最高权限用户,查看全部数据,不处理
        } else if (MEDICAL_ETHICS_ORDINARY_USER.equals(highestPermission)) {
            //普通用户,查看当前发薪号的数据
            returnMap.put("userId", user.getMoneycard());
        } else {
            //其他用户，如党委书记,查看对应支部下的数据
            MedicalEthicsUser medicalEthicsUser = mapper.list(new HashMap<String, String>() {{
                put("userId", user.getMoneycard());
            }}).get(0);
            returnMap.put("partyCommitteesId", String.valueOf(medicalEthicsUser.getPartyCommitteesId()));
            if (NUMBER_ZERO != medicalEthicsUser.getGeneralBranchId()) {
                //党总支默认0
                returnMap.put("generalBranchId", String.valueOf(medicalEthicsUser.getGeneralBranchId()));
            }
            if (NUMBER_ZERO != medicalEthicsUser.getBranchId()) {
                //党支部默认0
                returnMap.put("branchId", String.valueOf(medicalEthicsUser.getBranchId()));
            }
        }
        return returnMap;
    }

    @Transactional
    @Override
    public void batchInsert(List<MedicalEthicsUser> dataSource) {
        mapper.batchInsert(dataSource);
    }

    @Transactional
    @Override
    public void batchDelete(List<Integer> idList) {
        mapper.batchDelete(idList);
    }

    @Transactional
    @Override
    public void deleteByUserId(String userId) {
        mapper.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void batchInsertIntoUserRole(List<MedicalEthicsUserRole> dataSource) {
        mapper.batchInsertIntoUserRole(dataSource);
    }

    @Transactional
    @Override
    public void batchDeleteUserRole(List<String> userIds) {
        mapper.batchDeleteUserRole(userIds);
    }

    /**
     * 批量删除普通用户的角色
     * @param userIds
     */
    @Transactional
    @Override
    public void batchDeleteCommonUserRole(List<String> userIds) {
        mapper.batchDeleteCommonUserRole(userIds);
    }

    @Override
    public void deleteUserRoleByUserId(String userId) {
        mapper.deleteUserRoleByUserId(userId);
    }

    @Override
    public void deleteUserRoleByUserIdAndPartyId(String userId, Integer partyId) {
        mapper.deleteUserRoleByUserIdAndPartyId(userId, partyId);
    }

    @Override
    public MedicalEthicsRoleInfo getRoleInfo(Map<String, String> params) {
        return mapper.getRoleInfo(params);
    }

    /**
     * 临时用户资料更新
     */
    @Override
    public void updateRelations(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId, Integer personType) {
        //更新msg_temp表
        mapper.updateRelations(userId, partyCommitteesId, generalBranchId, branchId, personType);
        //更新msg表
    }

    /**
     * [非]临时用户的资料更新
     */
    @Override
    public void updateRelations2(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId, Integer personType) {
        mapper.updateRelations2(userId, partyCommitteesId, generalBranchId, branchId, personType);
    }


    /**
     * 检查用户是否已经存在于考核人员中
     *
     * @param medicalEthicsUser
     * @return
     */
    private Boolean checkExist(MedicalEthicsUser medicalEthicsUser) {
        //检查数据是否存在
        Map<String, String> params = new HashMap<>(5);
        String username = medicalEthicsUser.getUserName();
        String userId = medicalEthicsUser.getUserId();
        params.put("userName", username);
        params.put("userId", userId);
        List<MedicalEthicsUser> list = selectByParams(params);
        //人员存在于考核人员中
        return list.isEmpty();
    }

    /**
     * selectByParams 根据参数查询数据列表,非详情
     *
     * @param params 查询参数
     * @return {@link List<MedicalEthicsUser>}
     */
    @Override
    public List<MedicalEthicsUser> selectByParams(Map<String, String> params) {
        return mapper.selectByParams(params);
    }

    @Override
    public void addUserRole(MedicalEthicsUserRole userRole) {
        mapper.addUserRole(userRole);
    }

    @Override
    public void addUser(MedicalEthicsUser medicalEthicsUser) {
        mapper.addUser(medicalEthicsUser);
    }

    @Override
    public List<MedicalEthicsUserRole> selectUserRoleByUserId(String userId) {
        return mapper.selectUserRoleByUserId(userId);
    }

    @Override
    public List<MedicalEthicsUserRole> selectUserRoleByParams(Map<String, String> params) {
        return mapper.selectUserRoleByParams(params);
    }

    /**
     * 根据条件删除 考核用户-角色信息
     * @param prams
     */
    @Override
    public void deleteUserRoleByParams(Map<String, String> prams) {
        mapper.deleteUserRoleByParams(prams);
    }

    @Override
    public List<MedicalEthicsUser> listByUserId(List<String> userIdsList) {
        List<MedicalEthicsUser> dataSource = mapper.listByUserId(userIdsList);
        //处理返回人员类型
        dataSource.stream().forEach(m -> m.setPersonType(loadPersonType(m)));
        for (MedicalEthicsUser medicalEthicsUser: dataSource) {
            //处理人员类型
            medicalEthicsUser.setPersonType(loadPersonType(medicalEthicsUser));
        }
        return dataSource;
    }

    /**
     * 获取人员类型
     *
     * @return
     */
    public static int loadPersonType(MedicalEthicsUser medicalEthicsUser) {
        int personType;
        //(党委/党总支/党支部)
        if (medicalEthicsUser.getBranchId() != NUMBER_ZERO) {
            //存在党支部关系,取党支部人员类型
            personType = medicalEthicsUser.getBranchPersonType();
        } else if (medicalEthicsUser.getBranchId() == NUMBER_ZERO && medicalEthicsUser.getGeneralBranchId() != NUMBER_ZERO) {
            //不存在党支部关系,存在党总支关系,则取党总支人员类型
            personType = medicalEthicsUser.getGeneralBranchPersonType();
        } else {
            //只存在党委关系,则取党委人员类型
            personType = medicalEthicsUser.getPartyCommitteesPersonType();
        }
        return personType;
    }
}
