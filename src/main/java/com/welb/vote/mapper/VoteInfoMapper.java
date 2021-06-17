package com.welb.vote.mapper;

import com.welb.vote.entity.VoteInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteInfoMapper {

    VoteInfo selectByPrimaryKey(String workerId);

}
