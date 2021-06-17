package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String usercode);

    int insertSelective(User record);

    User selectByPrimaryKey(String usercode);

    List<User> selectAllUsers();

    User selectByMoneyCard(String moneycard);
    int updateByPrimaryKeySelective(User record);

    User findUserByUserCode(String userncode);

    User findRaterUserByUserCode(String userncode);

    int updateUserPassword(User user);

    String selectMaxUserCode();

    List<User>selectUserAll(User user);

    List<User>selectUserByStationCode(String stationcode);

    List<User>selectUserByDepartmentCode(String departmentcode);

    List<User>findUserAll();

    List<User>findUserAllBySummary();

    List<User>findFlagUsers();


    User findOne(String usercode);

    User selectPersonnelUserByMoneyCard(String moneycard);

    int deleteUserByMoneyCard(String moneycard);

    User findUserByOne(String usercode);

    List<User>getUserList(@Param("moneycard")String moneycard,@Param("departmentname") String departmentname);


    List<User>selectGradeUserList();

    User getUserByMoneyCard(String moneycard);

    User selectUserByMoneyCard(String moneycard);

    User getUserByUserCode(String usercode);

    void add(User user);
}
