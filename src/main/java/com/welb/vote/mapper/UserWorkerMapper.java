package com.welb.vote.mapper;

import com.welb.vote.entity.UserWorker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserWorkerMapper {

    int insertSelective(UserWorker userWorker);

    UserWorker selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(UserWorker userWorker);

    int selectCountByWorkerId(String userId);

    int selectCountByWorkerIdAndType(@Param("userId") String userId,@Param("type")String type);

    UserWorker selectVoteWorker(@Param("userId") String userId,@Param("workerId")String workerId);

    List<UserWorker> selectWorkerByWorkerId(String userId);

}
