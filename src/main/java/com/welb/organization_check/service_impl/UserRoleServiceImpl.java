package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.mapper.UserRoleMapper;
import com.welb.organization_check.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserRoleServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 用户角色业务层接口的实现类
 * @date 2019/5/240:23
 */
@Service
@Transactional
public class UserRoleServiceImpl implements IUserRoleService {
    @Resource
    UserRoleMapper userRoleMapper;

    @Override
    public int deleteByPrimaryKey(UserRoleKey key) {
        return userRoleMapper.deleteByPrimaryKey(key);
    }

    @Override
    public void insert(UserRoleKey userRoleKey) {
            userRoleMapper.insert(userRoleKey);
    }

    @Override
    public int insertSelective(UserRoleKey key) {
        return userRoleMapper.insertSelective(key);
    }

    @Override
    public List<UserRoleKey> selectUserRoleByUserCode(String usercode) {
        return userRoleMapper.selectUserRoleByUserCode(usercode);
    }

    @Override
    public int batchInsert(List<UserRoleKey> list) {
        return userRoleMapper.batchInsert(list);
    }

    @Override
    public UserRoleKey selectOne(UserRoleKey userRoleKey) {
        return userRoleMapper.selectOne(userRoleKey);
    }

    @Override
    public void batchDelete(List<UserRoleKey> dataSource) {
        userRoleMapper.batchDelete(dataSource);
    }


}
