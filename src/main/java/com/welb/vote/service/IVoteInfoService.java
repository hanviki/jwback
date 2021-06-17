package com.welb.vote.service;

import com.welb.vote.entity.VoteInfo;

/**
 * @author luoyaozu
 * @title: IVoteInfoService
 * @projectName xh-360appraisal-interface
 * @description: 优秀事迹接口
 * @date 2019/11/2614:55
 */
public interface IVoteInfoService {

    VoteInfo selectByPrimaryKey(String workerId);

}
