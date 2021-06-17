package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.AuthorizationUser;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IAuthorizationUserService
 * @projectName xh-360appraisal-interface
 * @description: 代理人表业务层接口
 * @date 2020/4/2711:36
 */
public interface IAuthorizationUserService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(AuthorizationUser authorizationUser);

    AuthorizationUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthorizationUser authorizationUser);

    int batchInsert(List<AuthorizationUser> list);

    List<AuthorizationUser>getAuthorizationList(AuthorizationUser authorizationUser);

    List<AuthorizationUser>getAuthorizationListByIsPersonnel(String ispersonnel,String departmentname);

    int batchDelete(List<Integer>ids);

    int updateFlag(String flag,String moneycard);

    int updateFlagByMoneyCard(String flag,String moneycard);

    AuthorizationUser selectByMoneyCard(String moneycard);
}
