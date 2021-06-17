package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteRules;
import com.welb.vote.mapper.VoteRulesMapper;
import com.welb.vote.service.IVoteRulesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: VoteRulesServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/11/2117:23
 */
@Service
@Transactional
public class VoteRulesServiceImpl implements IVoteRulesService {
    @Resource
    VoteRulesMapper voteRulesMapper;
    @Override
    public VoteRules selectByPrimaryKey(int id) {
        return voteRulesMapper.selectByPrimaryKey(id);
    }
}
