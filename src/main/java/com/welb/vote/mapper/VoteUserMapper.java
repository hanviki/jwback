package com.welb.vote.mapper;

import com.welb.vote.entity.VoteUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoteUserMapper {

    int updateByPrimaryKeySelective(VoteUser voteUser);

    int updateDCountById(@Param("dCount")int dCount,@Param("uId")String uId);

    int updateHCountById(@Param("hCount")int hCount,@Param("uId")String uId);

    VoteUser selectByPrimaryKey(String uId);

    List<VoteUser>selectAll();

    int batchUpdate(List<VoteUser>users);

    int insertSelective(VoteUser voteUser);




}
