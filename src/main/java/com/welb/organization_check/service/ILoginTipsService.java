package com.welb.organization_check.service;

import com.welb.organization_check.entity.LoginTips;

/**
 * @author luoyaozu
 * @title: LoginTipsService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/1811:55
 */
public interface ILoginTipsService {
    /**
     * 修改登录提示
     * @param loginTips
     * @return
     */
    int updateLoginTips(LoginTips loginTips);

    /**
     * 查询登陆提示
     * @return
     */
    LoginTips selectLoginTips(String rolecode);
}
