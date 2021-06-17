package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.AuthorizationUser;
import com.welb.personnel_check.entity.PersonnelAuthorization;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IAuthorizationUserService;
import com.welb.personnel_check.service.IPersonnelAuthorizationService;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: AuthorizationUserController
 * @projectName xh-360appraisal-interface
 * @description: 代理人控制器
 * @date 2020/4/2711:41
 */
@RestController
@RequestMapping("/authorizationUser")
public class AuthorizationUserController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Resource
    IAuthorizationUserService authorizationUserService;
    @Resource
    IUserService userService;
    @Resource
    IRaterService raterService;
    @Resource
    IPersonnelAuthorizationService personnelAuthorizationService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IPersonnelUserService personnelUserService;

    @RequestMapping("/getAuthorizationList")
    public Object getAuthorizationList(HttpServletRequest req, AuthorizationUser authorizationUser, String loginmoneycard, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                List<AuthorizationUser> authorizationList = null;
                boolean flag = true;//获取角色- ，不同的角色呈现的代理人列表是不同的  默认是部门长
                List<UserRoleKey> roleList = userRoleService.selectUserRoleByUserCode(usercode);
                for (UserRoleKey role : roleList) {
                    if (role.getRolecode().equals("400")) {
                        flag = false;
                        break;
                    }
                }
                if (flag == false) {//管理员
                    PageHelper.startPage(pageNum, pageSize);
                    authorizationList = authorizationUserService.getAuthorizationList(authorizationUser);
                } else {
                    List<AuthorizationUser> list = new ArrayList<>();
                    Rater rater = raterService.selectByMoneyCard(loginmoneycard);
                    authorizationUser.setDepartmentname(rater.getDepartment());
                    //把人事部该部门下的员工添加到授权表中
                    addPersonnelToAuthorization(list, rater);
                    List<AuthorizationUser>authorizations = authorizationUserService.getAuthorizationListByIsPersonnel("1",rater.getDepartment());
                    List<User> users = userService.getUserList(loginmoneycard,rater.getDepartment());
                    if (authorizations.size() >= 0 && authorizations.size() < users.size()) {
                        authorizationList = addLessAuthrozationUser(authorizationUser, pageNum, pageSize, list, rater, authorizations, users,authorizationList);
                    } else if (authorizations.size() > users.size()) {
                        authorizationList = deleteUnnecessaryAuthorization(authorizationUser, pageNum, pageSize, authorizations, users,authorizationList);
                    }else {
                        PageHelper.startPage(pageNum, pageSize);
                        authorizationList = authorizationUserService.getAuthorizationList(authorizationUser);
                    }
                }
                PageInfo<AuthorizationUser> pageInfo = new PageInfo<>(authorizationList);
                map.put("msg", "查询成功");
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", authorizationList);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void addPersonnelToAuthorization(List<AuthorizationUser> list, Rater rater) {
        List<PersonnelUser> personnelUserList = personnelUserService.selectListByDeptName(rater.getDepartment());
        List<AuthorizationUser>authorizations = authorizationUserService.getAuthorizationListByIsPersonnel("2",rater.getDepartment());
        if (authorizations.size() >= 0 && authorizations.size() < personnelUserList.size()){
            List<PersonnelUser> userList = new ArrayList<>();
            for (int i = 0; i < personnelUserList.size(); i++) {//找出需要添加的授权人
                for (int j = 0; j < authorizations.size(); j++) {
                    if (personnelUserList.get(i).getMoneycard().equals(authorizations.get(j).getMoneycard())) {
                        userList.add(personnelUserList.get(i));
                        break;
                    }
                }
            }
            personnelUserList.removeAll(userList);
            for (PersonnelUser personnel:personnelUserList){
                AuthorizationUser user = new AuthorizationUser();
                user.setIspersonnel("2");
                user.setDepartmentname(personnel.getDepartmentname());
                user.setFlag(personnel.getFlag());
                user.setStarttime(personnel.getStarttime());
                user.setEndtime(personnel.getEndtime());
                user.setUsername(personnel.getUsername());
                user.setMoneycard(personnel.getMoneycard());
                list.add(user);

            }
            if (list.size()>0){
                authorizationUserService.batchInsert(list);
                list.clear();
            }
        }else if (authorizations.size() > personnelUserList.size()){
            List<AuthorizationUser> authorizationUsers = new ArrayList<>();
            for (int i = 0; i < authorizations.size(); i++) {//因人员职位调动导致某部门会出现多余的授权人，找出并删除
                for (int j = 0; j < personnelUserList.size(); j++) {
                    if (authorizations.get(i).getMoneycard().equals(personnelUserList.get(j).getMoneycard())) {
                        authorizationUsers.add(authorizations.get(i));
                        break;
                    }
                }
            }
            authorizations.removeAll(authorizationUsers);
            List<Integer> ids = new ArrayList<>();
            for (AuthorizationUser user : authorizations) {
                ids.add(user.getId());
            }
            if (ids.size() > 0) {//批量删除多余的代理人
                authorizationUserService.batchDelete(ids);
            }
        }
    }

    private List<AuthorizationUser> deleteUnnecessaryAuthorization(AuthorizationUser authorizationUser, int pageNum, int pageSize, List<AuthorizationUser> authorizations, List<User> users,List<AuthorizationUser> authorizationList) {
        List<AuthorizationUser> authorizationUsers = new ArrayList<>();
        for (int i = 0; i < authorizations.size(); i++) {//因人员职位调动导致某部门会出现多余的授权人，找出并删除
            for (int j = 0; j < users.size(); j++) {
                if (authorizations.get(i).getMoneycard().equals(users.get(j).getMoneycard())) {
                    authorizationUsers.add(authorizations.get(i));
                    break;
                }
            }
        }
        authorizations.removeAll(authorizationUsers);
        List<Integer> ids = new ArrayList<>();
        for (AuthorizationUser user : authorizations) {
            ids.add(user.getId());
        }
        if (ids.size() > 0) {//批量删除多余的代理人
            authorizationUserService.batchDelete(ids);
        }
        PageHelper.startPage(pageNum, pageSize);
        authorizationList = authorizationUserService.getAuthorizationList(authorizationUser);
        return authorizationList;
    }

    private List<AuthorizationUser> addLessAuthrozationUser(AuthorizationUser authorizationUser, int pageNum, int pageSize, List<AuthorizationUser> list,
                                                            Rater rater, List<AuthorizationUser> authorizations, List<User> users,List<AuthorizationUser> authorizationList) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {//找出需要添加的授权人
            for (int j = 0; j < authorizations.size(); j++) {
                if (users.get(i).getMoneycard().equals(authorizations.get(j).getMoneycard())) {
                    userList.add(users.get(i));
                    break;
                }
            }
        }
        users.removeAll(userList);
        for (User one : users) {
            AuthorizationUser user = addAuthorization(rater, one);//新增授权人到集合
            list.add(user);
        }
        if (list.size() > 0) {//批量添加
            authorizationUserService.batchInsert(list);
        }
        PageHelper.startPage(pageNum, pageSize);
        authorizationList = authorizationUserService.getAuthorizationList(authorizationUser);
        return authorizationList;
    }

    private AuthorizationUser addAuthorization(Rater rater, User one) {
        AuthorizationUser user = new AuthorizationUser();
        user.setDepartmentname(rater.getDepartment());
        user.setMoneycard(one.getMoneycard());
        user.setUsername(one.getUsername());
        user.setFlag("3");
        user.setStarttime("");
        user.setEndtime("");
        user.setIspersonnel("1");
        return user;
    }


    /**
     * 获取历史代理用户
     *
     * @param pageNum
     * @param pageSize
     * @param authorization
     * @return
     */
    @RequestMapping("/getHistoryAuthorizationList")
    public Object getHistoryAuthorizationList(HttpServletRequest req, int pageNum, int pageSize, PersonnelAuthorization authorization, String loginmoneycard) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        try {
            if (usercode != null) {
                boolean flag = true;//获取角色- ，不同的角色呈现的代理人列表是不同的  默认是部门长
                List<UserRoleKey> roleList = userRoleService.selectUserRoleByUserCode(usercode);
                for (UserRoleKey role : roleList) {
                    if (role.getRolecode().equals("400")) {
                        flag = false;
                        break;
                    }
                }
                if (flag==true){
                    Rater rater = raterService.selectByMoneyCard(loginmoneycard);
                    authorization.setDepartmentname(rater.getDepartment());
                }
                PageHelper.startPage(pageNum, pageSize);
                List<PersonnelAuthorization> list= personnelAuthorizationService.getHistoryAuthorizationList(authorization);
                PageInfo<PersonnelAuthorization> pageInfo = new PageInfo<>(list);
                map.put("msg", "查询成功");
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", list);
                map.put("code", 0);
            } else {
                map.put("msg", "登录用户超时,请重新登录");
                map.put("code", 810);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }
}
