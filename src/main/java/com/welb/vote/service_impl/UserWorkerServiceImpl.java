package com.welb.vote.service_impl;

import com.welb.vote.entity.UserWorker;
import com.welb.vote.mapper.UserWorkerMapper;
import com.welb.vote.service.IUserWorkerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserWorkerServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 投票人与被投票人中间表接口的实现类
 * @date 2019/11/2214:08
 */
@Service
@Transactional
public class UserWorkerServiceImpl implements IUserWorkerService {
    @Resource
    UserWorkerMapper userWorkerMapper;
    @Override
    public int insertSelective(UserWorker userWorker) {
        return userWorkerMapper.insertSelective(userWorker);
    }

    @Override
    public UserWorker selectByPrimaryKey(int id) {
        return userWorkerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserWorker userWorker) {
        return userWorkerMapper.updateByPrimaryKeySelective(userWorker);
    }

    @Override
    public int selectCountByWorkerId(String userId) {
        return userWorkerMapper.selectCountByWorkerId(userId);
    }

    @Override
    public int selectCountByWorkerIdAndType(String userId, String type) {
        return userWorkerMapper.selectCountByWorkerIdAndType(userId, type);
    }

    @Override
    public UserWorker selectVoteWorker(String userId, String workerId) {
        return userWorkerMapper.selectVoteWorker(userId, workerId);
    }

    @Override
    public List<UserWorker> selectWorkerByWorkerId(String userId) {
        return userWorkerMapper.selectWorkerByWorkerId(userId);
    }
}
