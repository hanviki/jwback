package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteInfo;
import com.welb.vote.mapper.VoteInfoMapper;
import com.welb.vote.service.IVoteInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: VoteInfoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 优秀事迹接口得实现类
 * @date 2019/11/2614:58
 */
@Service
@Transactional
public class VoteInfoServiceImpl implements IVoteInfoService {
    @Resource
    VoteInfoMapper voteInfoMapper;
    @Override
    public VoteInfo selectByPrimaryKey(String workerId) {
        return voteInfoMapper.selectByPrimaryKey(workerId);
    }
}
