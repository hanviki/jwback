package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.Role;
import com.welb.organization_check.service.IRoleService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: RoleController
 * @projectName kao
 * @description: 角色控制类
 * @date 2019/5/1521:18
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IRoleService roleService;

    /**
     * 查询所有角色
     *
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectAll() {
        ModelMap map = new ModelMap();
        List<Role> roles;
        PageHelper.startPage(1, 3);
        try {
            roles = roleService.selectAll();
            PageInfo<Role> pageInfo = new PageInfo<>(roles);
            roles = pageInfo.getList();
            map.put("msg", "查询角色成功");
            map.put("totalpages", pageInfo.getPages());
            map.put("data", roles);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询角色失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addRole(Role role) {
        ModelMap map = new ModelMap();
        int count = roleService.addRole(role);
        if (count > 0) {
            map.put("msg", "添加角色成功");
            map.put("data", 0);
        } else {
            map.put("msg", "添加角色失败");
            map.put("data", 1);
        }
        return map;
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateRole(Role role) {
        ModelMap map = new ModelMap();
        int count = roleService.updateRole(role);
        if (count > 0) {
            map.put("msg", "修改角色成功");
            map.put("data", 0);
        } else {
            map.put("msg", "修改角色失败");
            map.put("data", 1);
        }
        return map;
    }

    /**
     * 通过rolecode删除角色
     *
     * @param rolecode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteRole(String rolecode) {
        ModelMap map = new ModelMap();
        int count = roleService.deleteRole(rolecode);
        if (count > 0) {
            map.put("msg", "删除角色成功");
            map.put("data", 0);
        } else {
            map.put("msg", "删除角色失败");
            map.put("data", 1);
        }
        return map;
    }
}
