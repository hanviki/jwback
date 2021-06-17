package com.welb.sysBase.service;

import com.welb.sysBase.mapper.PermissionMapper;
import com.welb.sysBase.util.Permission;
import com.welb.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService {
    @Autowired(required = false)
    private PermissionMapper mapper;

    @Override
    public List<PageData> showAllUser(Map<String, String> param) {
        return mapper.showAllUser(param);
    }

    @Override
    public List<PageData> showUserRole(Map<String, String> param) {
        return mapper.showUserRole(param);
    }

    @Override
    public List<PageData> showUser(Map<String, String> param) {
        return mapper.showUser(param);
    }

    @Override
    public List<PageData> showLoginUserByUserId(List<String> userIdList) {
        return mapper.showLoginUserByUserId(userIdList);
    }

    @Override
    public List<Permission> showAllData(Map<String, String> param) {
        return mapper.showAllData(param);
    }

    @Override
    public Permission showSingleRecord(String uId) {
        return mapper.showSingleRecord(uId);
    }

    @Override
    public void deleteUserRoleByUserCode(String userCode) {
        mapper.deleteUserRoleByUserCode(userCode);
    }

    @Override
    public List<PageData> showUserByUserId(List<String> userIdList) {
        return mapper.showUserByUserId(userIdList);
    }

    @Override
    public void updatePhone(String u_id, String u_phone) {
        mapper.updatePhone(u_id, u_phone);
    }

}
