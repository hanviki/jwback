package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.AuthorizationUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorizationUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(AuthorizationUser authorizationUser);

    AuthorizationUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthorizationUser authorizationUser);

    int batchInsert(List<AuthorizationUser>list);

    List<AuthorizationUser>getAuthorizationList(AuthorizationUser authorizationUser);

    List<AuthorizationUser>getAuthorizationListByIsPersonnel(@Param("ispersonnel")String ispersonnel,@Param("departmentname")String departmentname);

    int batchDelete(List<Integer>ids);

    int updateFlag(@Param("flag") String flag,@Param("moneycard") String moneycard);

    int updateFlagByMoneyCard(@Param("flag")String flag,@Param("moneycard")String moneycard);

    AuthorizationUser selectByMoneyCard(String moneycard);

}
