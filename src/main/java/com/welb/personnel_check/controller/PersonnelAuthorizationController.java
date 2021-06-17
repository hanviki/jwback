package com.welb.personnel_check.controller;

import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.AuthorizationUser;
import com.welb.personnel_check.entity.PersonnelAuthorization;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IAuthorizationUserService;
import com.welb.personnel_check.service.IPersonnelAuthorizationService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import com.welb.util.MD5;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

import static com.welb.sysBase.util.Constant.INITIAL_PASSWORD;

/**
 * @author luoyaozu
 * @title: PersonnelAuthorizationController
 * @projectName xh-360appraisal-interface
 * @description: 人事部各部门长授权代理人控制器
 * @date 2020/3/215:40
 */
@RestController
@RequestMapping("/authorization")
public class PersonnelAuthorizationController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    IPersonnelAuthorizationService authorizationService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IUserService userService;
    @Resource
    IRaterService raterService;
    @Resource
    IAuthorizationUserService authorizationUserService;

    /**
     * 开启授权接口，
     *
     * @param authorization
     * @return
     */
    @RequestMapping("/openAuthorization")
    public Object openAuthorization(PersonnelAuthorization authorization) {
        ModelMap map = new ModelMap();
        try {
            //获取当前系统时间
            String sysTime = DateUtil.getTime();
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(authorization.getEndtime()).getTime() ||
                    sdfTime.parse(authorization.getStarttime()).getTime() >= sdfTime.parse(authorization.getEndtime()).getTime()) {
                map.put("msg", "授权代理的结束时间不能低于当前时间或者低于授权代理的开始时间");
                map.put("code", 1);
            } else {
                AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(authorization.getAgent());
                authorization.setAgentusername(authorizationUser.getUsername());
                authorization.setIspersonnel(authorizationUser.getIspersonnel());
                if (sdfTime.parse(sysTime).getTime() < sdfTime.parse(authorization.getStarttime()).getTime()) {
                    authorization.setFlag("1"); //代理状态 1-代理前  2-代理中 3-停止代理
                    authorizationUser.setFlag("1");
                    User user = userService.selectByMoneyCard(authorization.getAgent());
                    User newUser = new User();
                    if (user == null) {
                        newUser.setMoneycard(authorization.getAgent());
                        newUser.setUsername(authorization.getAgentusername());
                        newUser.setPassword(MD5.md5(INITIAL_PASSWORD));//初始密码是whuh123456
                        newUser.setRoletype("1");//角色类型属于人事部
                        newUser.setFlag("0");
                        newUser.setUserstate("1");
                        userService.insertSelective(newUser);
                        map.put("msg", "操作成功，初始密码为whuh123456");
                    }else {
                        map.put("msg", "操作成功");
                    }
                } else {
                    authorization.setFlag("2");
                    authorizationUser.setFlag("2");
                    //给代理用户添加角色
                    addUserRole(authorization, map);
                }
                //修改代理人信息
                updateAuthorizationUser(authorization, authorizationUser);
                //添加人事部用户管理人员
                addRater(authorizationUser);
                authorization.setDepartmentname(authorizationUser.getDepartmentname());
                Rater rater = raterService.selectByMoneyCard(authorization.getDeptuser());
                authorization.setDeptusername(rater.getScorringname());
                //将年月日的时间格式改成年月日时分秒的格式
                authorization.setStarttime(authorization.getStarttime()+" 00:00:00");
                authorization.setEndtime(authorization.getEndtime()+" 23:59:59");
                authorizationService.insertSelective(authorization);
                //修改用户代理状态
                updateUser(authorization);
                map.put("code", 0);
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }

        return map;
    }

    private void addRater(AuthorizationUser user) {
        Rater rater = new Rater();
        rater.setDepartment(user.getDepartmentname());
        rater.setScorringcode(user.getMoneycard());
        rater.setScorringname(user.getUsername());
        rater.setFlag("1");
        raterService.insertSelective(rater);
    }

    private void updateUser(PersonnelAuthorization authorization) {
        User moneyCard = userService.selectByMoneyCard(authorization.getAgent());
        moneyCard.setIsagent("1");
        userService.updateByPrimaryKeySelective(moneyCard);
    }

    /**
     * 修改授权接口
     *
     * @param authorization
     * @return
     */
    @RequestMapping("/updateAuthorization")
    public Object updateAuthorization(PersonnelAuthorization authorization) {
        ModelMap map = new ModelMap();
        try {
            //获取当前系统时间
            String sysTime = DateUtil.getTime();
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(authorization.getEndtime()).getTime() ||
                    sdfTime.parse(authorization.getStarttime()).getTime() >= sdfTime.parse(authorization.getEndtime()).getTime()) {
                map.put("msg", "授权代理的结束时间不能低于当前时间或者低于授权代理的开始时间");
                map.put("code", 1);
            } else {
                AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(authorization.getAgent());
                if (sdfTime.parse(sysTime).getTime() < sdfTime.parse(authorization.getStarttime()).getTime()) {
                    authorization.setFlag("1");
                    authorizationUser.setFlag("1");
                    //删除代理人权限
                    deleteAuthrizationRole(authorization);
                } else{
                    authorization.setFlag("2");
                    authorizationUser.setFlag("2");
                    //给代理用户添加角色
                    addUserRole(authorization, map);
                }
                //修改代理用户表信息
                updateAuthorizationUser(authorization, authorizationUser);
                //修改授权日志表信息
                PersonnelAuthorization personnel = authorizationService.selectOne(authorization);
                authorization.setId(personnel.getId());
                Rater rater = raterService.selectByMoneyCard(authorization.getDeptuser());
                authorization.setDeptusername(rater.getScorringname());
                authorization.setStarttime(authorization.getStarttime()+" 00:00:00");
                authorization.setEndtime(authorization.getEndtime()+" 23:59:59");
                authorizationService.updateByPrimaryKeySelective(authorization);
                map.put("msg", "操作成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }


        return map;
    }

    private void deleteAuthrizationRole(PersonnelAuthorization authorization) {
        User user = userService.selectByMoneyCard(authorization.getAgent());
        UserRoleKey key = new UserRoleKey();
        key.setUsercode(user.getUsercode());
        key.setRolecode("500");
        userRoleService.deleteByPrimaryKey(key);
    }

    private void updateAuthorizationUser(PersonnelAuthorization authorization, AuthorizationUser user) {
        user.setStarttime(authorization.getStarttime());
        user.setEndtime(authorization.getEndtime());
        authorizationUserService.updateByPrimaryKeySelective(user);
    }

    private void addUserRole(PersonnelAuthorization authorization, ModelMap map) {
        UserRoleKey key = new UserRoleKey();
        User user = userService.selectByMoneyCard(authorization.getAgent());
        if (user == null) {
            User newUser = addUser(authorization);
            key.setUsercode(newUser.getUsercode());
            key.setRolecode("500");
            map.put("msg", "操作成功，初始密码为whuh123456");
        } else {
            map.put("msg", "操作成功");
            key.setUsercode(user.getUsercode());
            key.setRolecode("500");
        }
        UserRoleKey roleKey = userRoleService.selectOne(key);
        if (roleKey == null) {
            userRoleService.insertSelective(key);
        }
    }

    private User addUser(PersonnelAuthorization authorization) {
        User newUser = new User();
        newUser.setMoneycard(authorization.getAgent());
        newUser.setUsername(authorization.getAgentusername());
        newUser.setPassword(MD5.md5(INITIAL_PASSWORD));//初始密码是whuh123456
        newUser.setRoletype("1");//角色类型属于人事部
        newUser.setFlag("0");
        newUser.setUserstate("1");
        userService.insertSelective(newUser);
        return newUser;
    }


    /**
     * 取消授权接口
     *
     * @param authorization
     * @return
     */
    @RequestMapping("/closeAuthorization")
    public Object closeAuthorization(PersonnelAuthorization authorization) {
        ModelMap map = new ModelMap();
        try {
            //获取当前系统时间
            String sysTime = DateUtil.getTime();
            PersonnelAuthorization selectOne = authorizationService.selectOne(authorization);
            if (selectOne != null) {
                if (selectOne.getFlag().equals("1")) {
                    authorizationService.updateFlag("3", selectOne.getEndtime(), selectOne.getAgent());
                } else if (selectOne.getFlag().equals("2")) {
                    authorizationService.updateFlag("4", sysTime, selectOne.getAgent());
                }
                authorizationUserService.updateFlagByMoneyCard("3", selectOne.getAgent());
                User user = userService.selectByMoneyCard(authorization.getAgent());
                //删除代理人权限
                deleteRole(user);
                //修改用户表代理状态
                if (user.getRoletype().equals("0")) {
                    user.setIsagent("0");
                    userService.updateByPrimaryKeySelective(user);
                } else {
                    userService.deleteUserByMoneyCard(user.getMoneycard());
                }
                //删除管理用户
                raterService.deleteRaterByScorringCode(authorization.getAgent());
                map.put("msg", "取消授权成功");
                map.put("code", 0);
            } else {
                map.put("msg", "该用户还未授予代理权限或者代理权限已到期，无法进行取消授权操作");
                map.put("code", 0);
            }

        } catch (
                Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "取消授权失败");
            map.put("code", 1);
        }

        return map;
    }

    private void deleteRole(User user) {
        UserRoleKey key = new UserRoleKey();
        key.setRolecode("500");
        key.setUsercode(user.getUsercode());
        userRoleService.deleteByPrimaryKey(key);
    }
}
