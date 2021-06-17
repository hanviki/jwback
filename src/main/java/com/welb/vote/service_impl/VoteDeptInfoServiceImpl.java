package com.welb.vote.service_impl;

import com.welb.vote.entity.VoteDeptInfo;
import com.welb.vote.mapper.VoteDeptInfoMapper;
import com.welb.vote.service.IVoteDeptInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: VoteDeptInfoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 文明科室优秀事迹接口的实现类
 * @date 2019/11/2615:15
 */
@Service
@Transactional
public class VoteDeptInfoServiceImpl implements IVoteDeptInfoService {
    @Resource
    VoteDeptInfoMapper voteDeptInfoMapper;
    @Override
    public VoteDeptInfo selectByPrimaryKey(int deptId) {
        return voteDeptInfoMapper.selectByPrimaryKey(deptId);
    }
}
