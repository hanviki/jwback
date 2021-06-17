package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(String rolecode);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String rolecode);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role>selectAll();

    List<Role> selectRoleListByUserCode(String usercode);

    String selectMaxRoleCode();

}
