package com.welb.sysBase.service;

import com.welb.sysBase.util.Permission;
import com.welb.util.PageData;

import java.util.List;
import java.util.Map;

public interface IPermissionService {

    List<PageData> showAllUser(Map<String, String> param);

    List<PageData> showUserRole(Map<String, String> param);

    List<PageData> showUser(Map<String, String> param);

    List<PageData> showLoginUserByUserId(List<String> userIdList);

    List<Permission> showAllData(Map<String, String> param);

    Permission showSingleRecord(String uId);

    void deleteUserRoleByUserCode(String userCode);

    List<PageData> showUserByUserId(List<String> userIdList);

    void updatePhone(String u_id, String u_phone);
}
