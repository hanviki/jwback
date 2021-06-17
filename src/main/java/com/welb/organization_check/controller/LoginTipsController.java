package com.welb.organization_check.controller;

import com.welb.organization_check.entity.LoginTips;
import com.welb.organization_check.service.ILoginTipsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author luoyaozu
 * @title: LoginTipsController
 * @projectName xh-360appraisal-interface
 * @description: 登陆提示信息控制层
 * @date 2019/6/1811:57
 */
@RestController
@RequestMapping("/logintips")
public class LoginTipsController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    ILoginTipsService loginTipsService;

    /**
     * 查询登录提示信息
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectLoginTips(HttpServletRequest req, String rolecode) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            LoginTips loginTips = loginTipsService.selectLoginTips(rolecode);
            if (loginTips != null) {
                map.put("msg", "查询登录提示成功");
                map.put("data", loginTips);
                map.put("code", 0);
            } else {
                map.put("msg", "查询登录提示失败");
                map.put("code", 1);
            }

        } else {
            map.put("msg", "用户登录超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 修改登陆提示信息
     *
     * @param loginTips
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateLoginTips(LoginTips loginTips) {
        ModelMap map = new ModelMap();
        int count = loginTipsService.updateLoginTips(loginTips);
        LoginTips tips = new LoginTips();
        tips.setLogininfo(loginTips.getLogininfo());
        tips.setLogintype(loginTips.getLogintype());
        //角色是组织部
        if (loginTips.getRolecode().equals("100")) {
            tips.setRolecode("150");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("100")) {
            tips.setRolecode("200");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("100")) {
            tips.setRolecode("300");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("100")) {
            tips.setRolecode("50");
            loginTipsService.updateLoginTips(tips);
        }
        //角色是组织部部长
        if (loginTips.getRolecode().equals("50")) {
            tips.setRolecode("150");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("50")) {
            tips.setRolecode("200");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("50")) {
            tips.setRolecode("300");
            loginTipsService.updateLoginTips(tips);
        }
        if (loginTips.getRolecode().equals("50")) {
            tips.setRolecode("100");
            loginTipsService.updateLoginTips(tips);
        }
        //角色是人事部超级管理员
        if (loginTips.getRolecode().equals("400")) {
            tips.setRolecode("500");
            tips.setInportlogin(loginTips.getInportlogin());
            tips.setInportinfo(loginTips.getInportinfo());
            loginTipsService.updateLoginTips(tips);
        }
        if (count > 0) {
            map.put("msg", "修改登录提示成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改登录提示失败");
            map.put("code", 1);
        }
        return map;
    }
}
