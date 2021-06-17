package com.welb.vote.mapper;

import com.welb.vote.entity.VoteRules;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteRulesMapper {

    VoteRules selectByPrimaryKey(int id);

}
