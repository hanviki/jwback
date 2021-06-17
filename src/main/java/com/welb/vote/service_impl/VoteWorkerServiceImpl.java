package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteWorker;
import com.welb.vote.mapper.VoteWorkerMapper;
import com.welb.vote.service.IVoteWorkerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: VoteWorkerServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 医务工作者接口的实现类
 * @date 2019/11/2211:04
 */
@Service
@Transactional
public class VoteWorkerServiceImpl implements IVoteWorkerService {
    @Resource
    VoteWorkerMapper voteWorkerMapper;
    @Override
    public VoteWorker selectByPrimaryKey(String workerId) {
        return voteWorkerMapper.selectByPrimaryKey(workerId);
    }

    @Override
    public int updateByPrimaryKeySelective(VoteWorker voteWorker) {
        return voteWorkerMapper.updateByPrimaryKeySelective(voteWorker);
    }

    @Override
    public List<VoteWorker> selectAllWorkerByType(String type) {
        return voteWorkerMapper.selectAllWorkerByType(type);
    }

    @Override
    public int batchUpdate(List<String> workerIds) {
        return voteWorkerMapper.batchUpdate(workerIds);
    }

}
