package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.mapper.PersonnelUserMapper;
import com.welb.personnel_check.service.IPersonnelUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author luoyaozu
 * @title: PersonnelUserService
 * @projectName xh-360appraisal-interface
 * @description: 人事部用户业务层接口的实现类
 * @date 2019/9/211:11
 */
@Service
@Transactional
public class PersonnelUserService implements IPersonnelUserService {
    @Resource
    PersonnelUserMapper personnelUserMapper;
    @Override
    public int insertSelective(PersonnelUser personnelUser) {
        return personnelUserMapper.insertSelective(personnelUser);
    }

    @Override
    public List<PersonnelUser> selectAllPersonnelByLike(PersonnelUser personnelUser) {
        return personnelUserMapper.selectAllPersonnelByLike(personnelUser);
    }

    @Override
    public int deleteByPrimaryKey(String moneycard) {
        return personnelUserMapper.deleteByPrimaryKey(moneycard);
    }

    @Override
    public PersonnelUser selectByPrimaryKey(String moneycard) {
        return personnelUserMapper.selectByPrimaryKey(moneycard);
    }

    @Override
    public List<PersonnelUser> selectList() {
        return personnelUserMapper.selectList();
    }

    @Override
    public List<PersonnelUser> selectListByDeptName(String departmentname) {
        return personnelUserMapper.selectListByDeptName(departmentname);
    }

    @Override
    public int updateByPrimaryKeySelective(PersonnelUser personnelUser) {
        return personnelUserMapper.updateByPrimaryKeySelective(personnelUser);
    }

    @Override
    public int batchInsert(List<PersonnelUser> list) {
        return personnelUserMapper.batchInsert(list);
    }

    @Override
    public List<PersonnelUser> selectPersonnelByDepartmentName(String departmentname) {
        return personnelUserMapper.selectPersonnelByDepartmentName(departmentname);
    }

    @Override
    public List<PersonnelUser> selectListByPersonner(PersonnelUser personnelUser) {
        return personnelUserMapper.selectListByPersonner(personnelUser);
    }

    @Override
    public int updateFlagByMoneyCard(String flag, String moneycard) {
        return personnelUserMapper.updateFlagByMoneyCard(flag, moneycard);
    }
    @Override
    public int updateFlag(String flag, String moneycard) {
        return personnelUserMapper.updateFlag(flag, moneycard);
    }

    @Override
    public PersonnelUser selectById(Integer id) {
        return personnelUserMapper.selectById(id);
    }

    @Override
    public List<String> allUserIds() {
        return personnelUserMapper.allUserIds();
    }


}
