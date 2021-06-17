package com.welb.vote.service;

import com.welb.vote.entity.VoteWorker;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IVoteWorkerService
 * @projectName xh-360appraisal-interface
 * @description: 医务工作者接口
 * @date 2019/11/2211:02
 */
public interface IVoteWorkerService {

    VoteWorker selectByPrimaryKey(String workerId);

    int updateByPrimaryKeySelective(VoteWorker voteWorker);

    List<VoteWorker>selectAllWorkerByType(String type);

    int batchUpdate(List<String>list);


}
