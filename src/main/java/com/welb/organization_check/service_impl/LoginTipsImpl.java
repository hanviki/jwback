package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.LoginTips;
import com.welb.organization_check.mapper.LoginTipsMapper;
import com.welb.organization_check.service.ILoginTipsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: LoginTipsImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/1811:56
 */
@Service
@Transactional
public class LoginTipsImpl implements ILoginTipsService {
    @Resource
    LoginTipsMapper loginTipsMapper;
    @Override
    public int updateLoginTips(LoginTips loginTips) {
        return loginTipsMapper.updateLoginTips(loginTips);
    }

    @Override
    public LoginTips selectLoginTips(String rolecode) {
        return loginTipsMapper.selectLoginTips(rolecode);
    }
}
