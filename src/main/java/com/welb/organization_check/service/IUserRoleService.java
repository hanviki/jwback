package com.welb.organization_check.service;

import com.welb.organization_check.entity.UserRoleKey;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserRoleService
 * @projectName xh-360appraisal-interface
 * @description: 用户角色业务逻辑层接口
 * @date 2019/5/240:21
 */
public interface IUserRoleService {
    /**
     * 删除用户角色数据
     * @param key
     * @return
     */
    int deleteByPrimaryKey(UserRoleKey key);

    void insert(UserRoleKey userRoleKey);

    /**
     * 添加用户角色数据
     * @param key
     * @return
     */
    int insertSelective(UserRoleKey key);

    /**
     * 通过用户编号查找用户角色信息
     * @param usercode
     * @return
     */
    List<UserRoleKey> selectUserRoleByUserCode(String usercode);

    /**
     * 批量添加角色
     * @param list
     * @return
     */
    int batchInsert(List<UserRoleKey>list);

    UserRoleKey selectOne(UserRoleKey userRoleKey);

    void batchDelete(List<UserRoleKey> dataSource);

}
