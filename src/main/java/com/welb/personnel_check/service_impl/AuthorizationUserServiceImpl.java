package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.AuthorizationUser;
import com.welb.personnel_check.mapper.AuthorizationUserMapper;
import com.welb.personnel_check.service.IAuthorizationUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: AuthorizationUserServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/4/2711:38
 */
@Service
@Transactional
public class AuthorizationUserServiceImpl implements IAuthorizationUserService {
    @Resource
    AuthorizationUserMapper authorizationUserMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return authorizationUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(AuthorizationUser authorizationUser) {
        return authorizationUserMapper.insertSelective(authorizationUser);
    }

    @Override
    public AuthorizationUser selectByPrimaryKey(Integer id) {
        return authorizationUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AuthorizationUser authorizationUser) {
        return authorizationUserMapper.updateByPrimaryKeySelective(authorizationUser);
    }

    @Override
    public int batchInsert(List<AuthorizationUser> list) {
        return authorizationUserMapper.batchInsert(list);
    }

    @Override
    public List<AuthorizationUser> getAuthorizationList(AuthorizationUser authorizationUser) {
        return authorizationUserMapper.getAuthorizationList(authorizationUser);
    }

    @Override
    public List<AuthorizationUser> getAuthorizationListByIsPersonnel(String ispersonnel,String departmentname) {
        return authorizationUserMapper.getAuthorizationListByIsPersonnel(ispersonnel,departmentname);
    }

    @Override
    public int batchDelete(List<Integer> ids) {
        return authorizationUserMapper.batchDelete(ids);
    }

    @Override
    public int updateFlag(String flag, String moneycard) {
        return authorizationUserMapper.updateFlag(flag, moneycard);
    }

    @Override
    public int updateFlagByMoneyCard(String flag, String moneycard) {
        return authorizationUserMapper.updateFlagByMoneyCard(flag, moneycard);
    }

    @Override
    public AuthorizationUser selectByMoneyCard(String moneycard) {
        return authorizationUserMapper.selectByMoneyCard(moneycard);
    }
}
