package com.welb.vote.mapper;

import com.welb.vote.entity.VoteWorker;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteWorkerMapper {
    VoteWorker selectByPrimaryKey(String workerId);

    int updateByPrimaryKeySelective(VoteWorker voteWorker);

    List<VoteWorker>selectAllWorkerByType(String type);

    int batchUpdate(List<String>workerIds);


}
