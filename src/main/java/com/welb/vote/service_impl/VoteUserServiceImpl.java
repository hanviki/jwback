package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteUser;
import com.welb.vote.mapper.VoteUserMapper;
import com.welb.vote.service.IVoteUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: VoteUserServiceImpl
 * @projectName xh-360appraisal-interface
 * @description:  投票人信息接口的实现类
 * @date 2019/11/2210:12
 */
@Service
@Transactional
public class VoteUserServiceImpl implements IVoteUserService {
    @Resource
    VoteUserMapper voteUserMapper;


    @Override
    public int updateByPrimaryKeySelective(VoteUser voteUser) {
        return voteUserMapper.updateByPrimaryKeySelective(voteUser);
    }

    @Override
    public int updateDCountById(int dCount, String uId) {
        return voteUserMapper.updateDCountById(dCount,uId);
    }

    @Override
    public int updateHCountById(int hCount, String uId) {
        return voteUserMapper.updateHCountById(hCount, uId);
    }

    @Override
    public VoteUser selectByPrimaryKey(String uId) {
        return voteUserMapper.selectByPrimaryKey(uId);
    }

    @Override
    public List<VoteUser> selectAll() {
        return voteUserMapper.selectAll();
    }

    @Override
    public int insertSelective(VoteUser voteUser) {
        return voteUserMapper.insertSelective(voteUser);
    }

}
