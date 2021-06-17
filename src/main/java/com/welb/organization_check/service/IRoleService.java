package com.welb.organization_check.service;

import com.welb.organization_check.entity.Role;

import java.util.List;

/**
 * @author luoyaozu
 * @title: RoleService
 * @projectName kao
 * @description: 角色业务层接口
 * @date 2019/5/1521:03
 */
public interface IRoleService {
    int addRole(Role role);

    int updateRole(Role role);

    int deleteRole(String roleCode);

    List<Role>selectAll();

    List<Role> selectRoleListByUserCode(String usercode);
}
