package com.welb.vote.service;

import com.welb.vote.entity.UserDept;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserDeptService
 * @projectName xh-360appraisal-interface
 * @description: 投票人与文明科室中间表接口
 * @date 2019/11/2214:43
 */
public interface IUserDeptService {

    int insertSelective(UserDept userDept);

    UserDept selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(UserDept userDept);

    int selectCountByUserId(String userId);

    UserDept selectDept(String userId,int deptId);

    List<UserDept>selectDeptByUserId(String userId);



}
