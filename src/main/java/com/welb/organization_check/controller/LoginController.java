package com.welb.organization_check.controller;

import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.AuthorizationUser;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IAuthorizationUserService;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.CalendarUtil;
import com.welb.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author luoyaozu
 * @title: LoginController
 * @projectName kao
 * @description: 用户登录控制类
 * @date 2019/5/169:37
 */
@RestController
public class LoginController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserService userService;
    @Resource
    IRoleService roleService;
    @Resource
    IAssessmentStateService stateService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IPersonnelUserService personnelUserService;
    @Resource
    IUserRoleService userRoleService;

    @Resource
    IRaterService raterService;
    @Resource
    IAuthorizationUserService authorizationUserService;

    @Autowired
    MedicalEthicsUserService medicalEthicsUserService;
    /**
     * 用户登录
     *
     * @param moneycard
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", produces = "application/json;charset=utf-8")
    public Object login(String moneycard, String password, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        String passwordMd5 = MD5.md5(password);//将前端获取的password进行加密与数据库的密码进行比对
        User user = userService.selectUserByMoneyCard(moneycard);
        if (user == null || user.getFlag().equals("1")) {
            map.put("msg", "用户不存在");
            map.put("code", 1);
        } else if (user.getUserstate().equals("0")) {
            map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
            map.put("code", 1);
        } else if (moneycard.equals("") || moneycard == null) {
            map.put("msg", "用户名不能为空");
            map.put("code", 1);
        } else if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            map.put("code", 1);
        } else if (!passwordMd5.equals(user.getPassword())) {
            map.put("msg", "密码错误");
            map.put("code", 1);
        } else {
            getLoginUserInfo(req, map, user);
        }
        return map;
    }

    private void getLoginUserInfo(HttpServletRequest req, ModelMap map, User user) {
        //将用户编号保存在session中
        req.getSession().setAttribute("usercode", user.getUsercode());
        //设置登录用户的过期时间
        req.getSession().setMaxInactiveInterval(30 * 60);
        //获取手动考核按钮状态
        AssessmentState state = stateService.selectByPrimaryKey(1);
        req.getSession().setAttribute("state",state.getState());
        //获取手动考核设置的考核时间
        String time = getTime(state);

        //查询医德医风的角色列表
        String moneyCard = user.getMoneycard();
        List<MedicalEthicsUserRole> userRoleList = medicalEthicsUserService.selectUserRoleByUserId(moneyCard);
        if (userRoleList != null && !userRoleList.isEmpty()) {
            List<String> roleList= new ArrayList<>();
            userRoleList.forEach(u ->{
               if(!roleList.contains(u.getRoleCode())){
                   roleList.add(u.getRoleCode());
               }
            });
            user.setMedicalEthicsRoleList(roleList);
        }

        List<Role> roles = roleService.selectRoleListByUserCode(user.getUsercode());
        int flagStatus =getFlag(user);
        if (roles.size() == 1) {
            user.setRolecode(roles.get(0).getRolecode());
            user.setRolename(roles.get(0).getRolename());
            user.setPassword("****");
            map.put("msg", "登录成功");
            map.put("flagStatus",flagStatus);
            map.put("state",state.getState());
            map.put("time",time);
            map.put("data", user);
            map.put("code", 0);

        } else if (roles.size() > 1) {
            user.setRoleList(roles);
            user.setRolecode("");
            user.setRolename("");
            user.setPassword("****");
            map.put("msg", "登录成功");
            map.put("flagStatus",flagStatus);
            map.put("state",state.getState());
            map.put("time",time);
            map.put("data", user);
            map.put("code", 0);
        } else {
            if (user.getIsagent().equals("0")) {
                map.put("msg", "该用户没有角色，无法登录，请联系管理员");
            }else {
                AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(user.getMoneycard());
                map.put("msg", "你指定的授权考核时间为("+authorizationUser.getStarttime()+"到"+authorizationUser.getEndtime()+"),请在这个时间范围内登录系统");
            }
            map.put("code", 1);
        }
    }

    private int getFlag(User user) {
        Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
        //判断管理员是不是人事部
        int flagStatus=0;//0-不是管理员  1-人事部管理员  2-非人事部管理员
        List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(user.getUsercode());
        if (rater!=null){
            for (UserRoleKey roleKey:userRoleKeys){
                if (roleKey.getRolecode().equals("400")){
                   if (rater.getDepartment().equals("人事处")){
                       flagStatus=1;
                       break;
                   }else {
                       flagStatus=2;
                   }
                }
            }
        }else {
            for (UserRoleKey roleKey:userRoleKeys){
                if (roleKey.getRolecode().equals("400")){
                    flagStatus=2;
                   break;
                }
            }
        }
        return flagStatus;
    }

    private String getTime(AssessmentState state) {
        //获取当前年份
        String year = CalendarUtil.getYear();
        //获取当前月份
        String month = CalendarUtil.getMonth();
        //获取当前季度
        String quarter = CalendarUtil.getQuarter(month);
        //当前上一个季度
        int count = Integer.parseInt(quarter.trim()) - 1;
        String time = null;
        //自动考核
        if (state.getState().equals("1")) {
            //手动考核
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
            if (setTime != null) {
                time=setTime.getTime();
            }else {
                if (count == 0) {
                    int lastyear = Integer.parseInt(year.trim()) - 1;
                    year = String.valueOf(lastyear);
                    month = "4";
                } else {
                    month = String.valueOf(count);
                }
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
                time=manualSetTime.getTime();
            }
        } else {
            time="";

        }
        return time;
    }

    @RequestMapping(value = "/singlelogion", produces = "application/json;charset=utf-8")
    public Object singleLogin(HttpServletRequest req, String usercode) {
        ModelMap map = new ModelMap();
        User user = userService.selectUserByMoneyCard(usercode);
        if (user == null || user.getFlag().equals("1")) {
            map.put("msg", "用户不存在");
            map.put("code", 1);
        } else if (user.getUserstate().equals("0")) {
            map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
            map.put("code", 1);
        } else {
            getLoginUserInfo(req, map, user);
        }
        return map;

    }

}
