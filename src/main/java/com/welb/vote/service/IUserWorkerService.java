package com.welb.vote.service;

import com.welb.vote.entity.UserWorker;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserWorkerService
 * @projectName xh-360appraisal-interface
 * @description: 投票人与被投票人中间表
 * @date 2019/11/2214:02
 */
public interface IUserWorkerService {

    int insertSelective(UserWorker userWorker);

    UserWorker selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(UserWorker userWorker);

    int selectCountByWorkerId(String userId);

    int selectCountByWorkerIdAndType(String userId,String type);

    UserWorker selectVoteWorker(String userId,String workerId);

    List<UserWorker> selectWorkerByWorkerId(String userId);


}
