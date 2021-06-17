package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey userRoleKey);

    List<UserRoleKey>selectUserRoleByUserCode(String usercode);

    int batchInsert(List<UserRoleKey>list);

    UserRoleKey selectOne(UserRoleKey userRoleKey);

    void batchDelete(List<UserRoleKey> dataSource);

}
