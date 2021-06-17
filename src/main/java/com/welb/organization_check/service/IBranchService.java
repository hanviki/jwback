package com.welb.organization_check.service;

import com.welb.organization_check.entity.Branch;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IBranchService
 * @projectName kao
 * @description: 支部管理业务成接口
 * @date 2019/5/2014:57
 */
public interface IBranchService {
    /**
     * 通过branchcode删除支部
     * @param branchcode
     * @return
     */
    int deleteByPrimaryKey(String branchcode);

    /**
     * 添加支部
     * @param branch
     * @return
     */
    int insertSelective(Branch branch);

    /**
     * 通过branchcode查找支部信息
     * @param branchcode
     * @return
     */
    Branch selectByPrimaryKey(String branchcode);

    /**
     * 修改支部信息
     * @param branch
     * @return
     */
    int updateByPrimaryKeySelective(Branch branch);

    /**
     * 查找所有支部信息
     * @return
     */
    List<Branch>selectBranchTrees(Branch branch);

}
