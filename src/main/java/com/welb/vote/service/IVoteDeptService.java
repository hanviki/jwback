package com.welb.vote.service;

import com.welb.vote.entity.VoteDept;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IVoteDeptService
 * @projectName xh-360appraisal-interface
 * @description: 文明科室接口
 * @date 2019/11/2211:33
 */
public interface IVoteDeptService {
    VoteDept selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(VoteDept voteDept);

    List<VoteDept> selectAllDept();

    int batchUpdate(List<Integer>ids);


}
