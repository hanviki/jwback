package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Branch;
import com.welb.organization_check.mapper.BranchMapper;
import com.welb.organization_check.service.IBranchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: BranchServiceImpl
 * @projectName kao
 * @description: 支部管理业务层接口的实现类
 * @date 2019/5/2015:01
 */
@Service
@Transactional
public class BranchServiceImpl implements IBranchService {
    @Resource
    BranchMapper branchMapper;
    @Override
    public int deleteByPrimaryKey(String branchcode) {
        return branchMapper.deleteByPrimaryKey(branchcode);
    }

    @Override
    public int insertSelective(Branch branch) {
        //实现branchcode自增
        String branchCode = branchMapper.selectMaxBranchCode();
        if (branchCode==null||branchCode==""){
            branch.setBranchcode("100");
        }else {
            int num = Integer.parseInt(branchCode.trim());
            num++;
            String branchcode = String.valueOf(num);
            branch.setBranchcode(branchcode);
        }
        return branchMapper.insertSelective(branch);
    }

    @Override
    public Branch selectByPrimaryKey(String branchcode) {
        return branchMapper.selectByPrimaryKey(branchcode);
    }

    @Override
    public int updateByPrimaryKeySelective(Branch branch) {
        return branchMapper.updateByPrimaryKeySelective(branch);
    }

    @Override
    public List<Branch> selectBranchTrees(Branch branch) {
        return branchMapper.selectBranchTrees(branch);
    }


}
