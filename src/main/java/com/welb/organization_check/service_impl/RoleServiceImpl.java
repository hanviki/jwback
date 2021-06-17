package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Role;
import com.welb.organization_check.mapper.RoleMapper;
import com.welb.organization_check.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: RoleServiceImpl
 * @projectName kao
 * @description: 角色业务层接口的实现类
 * @date 2019/5/1521:08
 */
@Service(value = "roleService")
@Transactional
public class RoleServiceImpl implements IRoleService {
    @Resource
    private RoleMapper roleMapper;
    @Override
    public int addRole(Role role) {
        //实现rolecode自增
        String roleCode = roleMapper.selectMaxRoleCode();
        if (roleCode==null){
            role.setRolecode("100");
        }else {
            int num = Integer.parseInt(roleCode.trim());
            num++;
            String rolecode = String.valueOf(num);
            role.setRolecode(rolecode);
        }
        return roleMapper.insertSelective(role);
    }

    @Override
    public int updateRole(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int deleteRole(String roleCode) {
        return roleMapper.deleteByPrimaryKey(roleCode);
    }

    @Override
    public List<Role> selectAll() {
        List<Role> roles = roleMapper.selectAll();
        return roles;
    }

    @Override
    public List<Role> selectRoleListByUserCode(String usercode) {
        return roleMapper.selectRoleListByUserCode(usercode);
    }
}
