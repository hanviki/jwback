package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.HrpUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HrpUserMapper {
    int deleteByPrimaryKey(String uId);

    int insertSelective(HrpUser hrpUser);

    HrpUser selectByPrimaryKey(String uId);

    int updateByPrimaryKeySelective(HrpUser hrpUser);

    List<HrpUser>selectAll();

}
