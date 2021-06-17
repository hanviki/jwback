package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteDept;
import com.welb.vote.mapper.VoteDeptMapper;
import com.welb.vote.service.IVoteDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: VoteDeptServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/11/2211:37
 */
@Service
@Transactional
public class VoteDeptServiceImpl implements IVoteDeptService {
    @Resource
    VoteDeptMapper voteDeptMapper;
    @Override
    public VoteDept selectByPrimaryKey(int id) {
        return voteDeptMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(VoteDept voteDept) {
        return voteDeptMapper.updateByPrimaryKeySelective(voteDept);
    }

    @Override
    public List<VoteDept> selectAllDept() {
        return voteDeptMapper.selectAllDept();
    }

    @Override
    public int batchUpdate(List<Integer> ids) {
        return voteDeptMapper.batchUpdate(ids);
    }
}
