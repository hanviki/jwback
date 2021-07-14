package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.User;
import com.welb.organization_check.mapper.UserMapper;
import com.welb.organization_check.service.IUserService;
import com.welb.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.welb.sysBase.util.Constant.INITIAL_PASSWORD;

/**
 * @author luoyaozu
 * @title: UserService
 * @projectName kao
 * @description: 用户业务层接口的实现类
 * @date 2019/5/160:40
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User findUserByUserCode(String usercode) {
        return userMapper.selectByPrimaryKey(usercode);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public List<User> selectUserAll(User user) {
        return userMapper.selectUserAll(user);
    }

    @Override
    public int deleteByPrimaryKey(String usercode) {
        return userMapper.deleteByPrimaryKey(usercode);
    }

    @Override
    public int insertSelective(User user) {
        //实现主键usercode自增
        String userCode = userMapper.selectMaxUserCode();
        if (userCode==null){
            user.setUsercode("100");
        }else {

            int num = Integer.parseInt(userCode.trim());
            num++;
            String usercode = String.valueOf(num);
            user.setUsercode(usercode);
        }
        //新添加的数据是初始密码为whuh123456
        user.setPassword(INITIAL_PASSWORD);
        //对密码进行加密
        String password = MD5.md5(user.getPassword());
        user.setPassword(password);
        user.setFlag("0");
        return userMapper.insertSelective(user);
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateUserPassword(User user) {
        return userMapper.updateUserPassword(user);
    }

    @Override
    public List<User> selectUserByStationCode(String stationcode) {
        return userMapper.selectUserByStationCode(stationcode);
    }

    @Override
    public List<User> selectUserByDepartmentCode(String departmentcode) {
        return userMapper.selectUserByDepartmentCode(departmentcode);
    }

    @Override
    public User selectByMoneyCard(String moneycard) {
        return userMapper.selectByMoneyCard(moneycard);
    }

    @Override
    public List<User> findUserAll() {
        return userMapper.findUserAll();
    }

    @Override
    public List<User> findFlagUsers() {
        return userMapper.findFlagUsers();
    }

    @Override
    public User findOne(String usercode) {
        return userMapper.findOne(usercode);
    }

    @Override
    public User findRaterUserByUserCode(String userncode) {
        return userMapper.findRaterUserByUserCode(userncode);
    }

    @Override
    public User selectPersonnelUserByMoneyCard(String moneycard) {
        return userMapper.selectPersonnelUserByMoneyCard(moneycard);
    }

    @Override
    public int deleteUserByMoneyCard(String moneycard) {
        return userMapper.deleteUserByMoneyCard(moneycard);
    }

    @Override
    public User findUserByOne(String usercode) {
        return userMapper.findUserByOne(usercode);
    }

    @Override
    public List<User> getUserList(String moneycard,String departmentname) {
        return userMapper.getUserList(moneycard,departmentname);
    }

    @Override
    public List<User> findUserAllBySummary() {
        return userMapper.findUserAllBySummary();
    }

    @Override
    public List<User> selectGradeUserList() {
        return userMapper.selectGradeUserList();
    }

    @Override
    public User getUserByMoneyCard(String moneycard) {
        return userMapper.getUserByMoneyCard(moneycard);
    }

    @Override
    public User selectUserByMoneyCard(String moneycard) {
        return userMapper.selectUserByMoneyCard(moneycard);
    }

    @Override
    public User getUserByUserCode(String usercode) {
        return userMapper.getUserByUserCode(usercode);
    }

    @Override
    public void add(User user) {
        userMapper.add(user);
    }

    @Override
    public List<User>findUserByScoreFlowType(String mserialNo, String scoreType){
        return  userMapper.findUserByScoreFlowType(mserialNo,scoreType);
    }
}
