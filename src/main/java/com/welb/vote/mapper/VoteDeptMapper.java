package com.welb.vote.mapper;

import com.welb.vote.entity.VoteDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteDeptMapper {

    VoteDept selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(VoteDept voteDept);

    List<VoteDept> selectAllDept();

    int batchUpdate(List<Integer>ids);


}
