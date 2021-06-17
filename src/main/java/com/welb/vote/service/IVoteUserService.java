package com.welb.vote.service;

import com.welb.vote.entity.VoteUser;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IVoteUserService
 * @projectName xh-360appraisal-interface
 * @description: 投票人信息接口
 * @date 2019/11/2210:09
 */
public interface IVoteUserService {

    int updateByPrimaryKeySelective(VoteUser voteUser);

    int updateDCountById(int dCount,String uId);

    int updateHCountById(int hCount,String uId);

    VoteUser selectByPrimaryKey(String uId);

    List<VoteUser> selectAll();

    int insertSelective(VoteUser voteUser);


}
