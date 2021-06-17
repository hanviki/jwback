package com.welb.vote.mapper;

import com.welb.vote.entity.UserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDeptMapper {

    int insertSelective(UserDept userDept);

    UserDept selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(UserDept userDept);

    int selectCountByUserId(String userId);

    UserDept selectDept(@Param("userId")String userId,@Param("deptId")int deptId);

    List<UserDept> selectDeptByUserId(String userId);

}
