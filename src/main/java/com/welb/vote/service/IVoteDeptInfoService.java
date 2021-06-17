package com.welb.vote.service;

import com.welb.vote.entity.VoteDeptInfo;

/**
 * @author luoyaozu
 * @title: IVoteDeptInfoService
 * @projectName xh-360appraisal-interface
 * @description: 文明科室优秀事迹接口
 * @date 2019/11/2615:15
 */
public interface IVoteDeptInfoService {

    VoteDeptInfo selectByPrimaryKey(int deptId);

}
