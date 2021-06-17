package com.welb.vote.mapper;

import com.welb.vote.entity.VoteDeptInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteDeptInfoMapper {

    VoteDeptInfo selectByPrimaryKey(int deptId);

}
