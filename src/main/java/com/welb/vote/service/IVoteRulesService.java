package com.welb.vote.service;

import com.welb.vote.entity.VoteRules;

/**
 * @author luoyaozu
 * @title: IVoteRulesService
 * @projectName xh-360appraisal-interface
 * @description: 鋀票规则接口
 * @date 2019/11/2117:18
 */
public interface IVoteRulesService {

    VoteRules selectByPrimaryKey(int id);
}
