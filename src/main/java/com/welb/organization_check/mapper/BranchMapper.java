package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Branch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BranchMapper {
    int deleteByPrimaryKey(String branchcode);

    int insert(Branch record);

    int insertSelective(Branch record);

    Branch selectByPrimaryKey(String branchcode);

    int updateByPrimaryKeySelective(Branch record);

    int updateByPrimaryKey(Branch record);

    List<Branch>selectBranchTrees(Branch branch);

    String selectMaxBranchCode();
}
