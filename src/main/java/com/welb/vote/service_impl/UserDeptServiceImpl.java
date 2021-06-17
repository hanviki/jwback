package com.welb.vote.service_impl;

import com.welb.vote.entity.UserDept;
import com.welb.vote.mapper.UserDeptMapper;
import com.welb.vote.service.IUserDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDeptServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 投票人与文明科室中间表接口的实现类
 * @date 2019/11/2214:44
 */
@Service
@Transactional
public class UserDeptServiceImpl implements IUserDeptService {
    @Resource
    UserDeptMapper userDeptMapper;
    @Override
    public int insertSelective(UserDept userDept) {
        return userDeptMapper.insertSelective(userDept);
    }

    @Override
    public UserDept selectByPrimaryKey(int id) {
        return userDeptMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserDept userDept) {
        return userDeptMapper.updateByPrimaryKeySelective(userDept);
    }

    @Override
    public int selectCountByUserId(String userId) {
        return userDeptMapper.selectCountByUserId(userId);
    }

    @Override
    public UserDept selectDept(String userId, int deptId) {
        return userDeptMapper.selectDept(userId, deptId);
    }

    @Override
    public List<UserDept> selectDeptByUserId(String userId) {
        return userDeptMapper.selectDeptByUserId(userId);
    }

}
