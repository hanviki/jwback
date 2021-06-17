package com.welb.sysBase.controller;

import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.service.IPermissionService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.KeyAndValue;
import com.welb.sysBase.util.PageUtils;
import com.welb.sysBase.util.Permission;
import com.welb.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    private IPermissionService service;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IUserService userService;

    /**
     * 查询所有权限数据
     */
    @RequestMapping("/showAllData")
    public Object showAllData(HttpServletRequest request){
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            //验证head中的u_id
            List<PageData> roleList = service.showUserRole(new HashMap<String, String>(){{put("user_code", param.get("u_id"));}}).stream().filter(r-> r.getString("RoleCode").equals("50")).collect(Collectors.toList());
            if (StringUtil.isNotEmpty(param.get("u_id")) && !roleList.isEmpty()){
                if (StringUtil.isNotEmpty(param.get("f_condition"))){
                    //处理查询条件空格
                    param.put("f_condition", param.get("f_condition").replaceAll(SPACE_STR, EMPTY_STR));
                }
                //用户的发薪号定义为uId,与head的u_id区别
                List<Permission> resultList = service.showAllData(param);
                List<Permission> listData = PageUtils.PaginationSetting(param, resultList);
                map.put("draw", param.get("draw"));
                map.put("recordsTotal", resultList.size()); //总条数
                map.put("recordsFiltered", listData.size()); //显示总条数
                map.put("data", listData);
                map.put("msg", "查询成功");
                map.put("code", 0);
            }
        } catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("msg", "异常");
            map.put("code", 4);
        }
        return ajaxJson(map);
    }

    /**
     * 组装数据
     */
    private List<PageData> handlePermission() {
        //全部用户
        List<PageData> allUserList = service.showAllUser(new HashMap<>());
        //用户权限角色
        List<PageData> allUserRoleList = service.showUserRole(new HashMap<>());
        //组装权限用户
        List<PageData> userList = service.showUser(new HashMap<>());
        userList.stream().forEach(u-> {
            List<String> permissionList = new ArrayList<>();
            allUserRoleList.stream().forEach(r->{
                if (u.getString("UserCode").equals(r.getString("UserCode"))){
                    permissionList.add(r.getString("RoleName"));
                }
                u.put("permission", permissionList);
            });
        });


        allUserList.stream().forEach(a-> userList.stream().forEach(u ->{
            if (a.getString("u_id").equals(u.getString("MoneyCard"))){
                a.put("permission", u.get("permission"));
            }
        }));

        return allUserList;
    }

    /**
     * 查询单条数据
     */
    @RequestMapping("/showDetails")
    public Object showDetails(HttpServletRequest request){
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String uId = param.get("uId");
            if (StringUtils.isNotEmpty(uId)){
                Permission permission = service.showSingleRecord(uId);
                List<KeyAndValue> personPermission = new ArrayList<>();
                List<KeyAndValue> organizationPermission = new ArrayList<>();
                List<KeyAndValue> questionnairePermission = new ArrayList<>();
                String permissionStr = permission.getRole_code();
                this.loadPermissionMap(personPermission, organizationPermission, questionnairePermission, permissionStr);
                permission.setPerson_permission(personPermission);
                permission.setOrganization_permission(organizationPermission);
                permission.setQuestionnaire_permission(questionnairePermission);
                map.put("data", permission);
            }
            map.put("msg", "请求成功");
            map.put("code", 0);
        } catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("msg", "异常");
            map.put("code", 4);
        }
        return ajaxJson(map);
    }

    /**
     * 更新权限
     */
    @RequestMapping("/operation")
    public Object operation(HttpServletRequest request){
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String uId = param.get("uId");
            String person_permission = param.get("person_permission");
            String organization_permission = param.get("organization_permission");
            String questionnaire_permission = param.get("questionnaire_permission");

            if (StringUtil.isNotEmpty(uId)){
                //当前用户(通过用户的发薪号uId查询)
                Permission permission = service.showSingleRecord(uId);
                String userCode = permission.getUser_code();
                //查询已有权限
                List<PageData> roleList = service.showUserRole(new HashMap<String, String>(){{put("user_code", userCode);}});
                List<PageData> checkList = roleList.stream().filter(o -> o.getString("UserCode").equals(userCode)).collect(Collectors.toList());
                List<UserRoleKey> insertList = new ArrayList<>();
                String tempUserCode = null;
                if (checkList != null && !checkList.isEmpty()){
                    tempUserCode = userCode;
                    //删除当前用户权限
                    service.deleteUserRoleByUserCode(tempUserCode);
                }else {
                    //新增用户
                    User user = new User();
                    user.setUsername(permission.getU_name());
                    user.setPassword(MD5.md5(INITIAL_PASSWORD));
                    user.setMobile(permission.getU_tel());
                    user.setNation(permission.getU_nation());
                    user.setEmail(permission.getU_email());
                    user.setMoneycard(String.valueOf(permission.getU_id()));
                    user.setSex("男".equals(permission.getU_sex()) ? "1" : "2");
                    //默认给为问卷调查类型(0-组织部  1-人事部  2-调查问卷')
                    user.setRoletype(USER_TYPE_QUESTIONNAIRE);
                    userService.insertSelective(user);
                    tempUserCode = user.getUsercode();
                }

                //更新权限
                this.loadData(tempUserCode, insertList, person_permission);  //人事部

                this.loadData(tempUserCode, insertList, organization_permission);  //组织部

                this.loadData(tempUserCode, insertList, questionnaire_permission);  //问卷

                if (insertList.isEmpty()){
                    //删除用户表
                    userService.deleteByPrimaryKey(tempUserCode);
                }else {
                    //批量插入
                    userRoleService.batchInsert(insertList);
                }
            }
            map.put("msg", "请求成功");
            map.put("code", 0);
        } catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("msg", "异常");
            map.put("code", 4);
        }
        return ajaxJson(map);
    }

    private void loadData(String userCode, List<UserRoleKey> insertList, String permission) {
        if (StringUtils.isNotEmpty(permission) && StringUtils.isNotEmpty(userCode)){
            String[] permissions = permission.split(COMMA);
            for (int i = 0; i < permissions.length; i++){
                UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setUsercode(userCode);
                userRoleKey.setRolecode(permissions[i]);
                insertList.add(userRoleKey);
            }
        }
    }

    private void loadPermissionMap(List<KeyAndValue> personPermission, List<KeyAndValue> organizationPermission, List<KeyAndValue> questionnairePermission, String permissionStr) {
        if (StringUtil.isNotEmpty(permissionStr)){
            String[] permissions = permissionStr.split(COMMA);
            for (int i =0 ; i < permissions.length; i++){
                String tempPermission = permissions[i];
                //人事
                for (Map.Entry<String, String> entry : PERSON_PERMISSION_MAP.entrySet()){
                    if (entry.getKey().equals(tempPermission)){
                        personPermission.add(new KeyAndValue(entry.getKey(), entry.getValue()));
                    }
                }

                //组织
                for (Map.Entry<String, String> entry : ORGANIZATION_PERMISSION_MAP.entrySet()){
                    if (entry.getKey().equals(tempPermission)){
                        organizationPermission.add(new KeyAndValue(entry.getKey(), entry.getValue()));
                    }
                }

                //问卷
                for (Map.Entry<String, String> entry : QUESTIONNAIRE_PERMISSION_MAP.entrySet()){
                    if (entry.getKey().equals(tempPermission)){
                        questionnairePermission.add(new KeyAndValue(entry.getKey(), entry.getValue()));
                    }
                }
            }
        }
    }

}
