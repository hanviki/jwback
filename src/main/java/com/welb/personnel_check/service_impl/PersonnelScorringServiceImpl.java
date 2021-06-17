package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.PersonnelScorring;
import com.welb.personnel_check.mapper.PersonnelScorringMapper;
import com.welb.personnel_check.service.IPersonnelScorringService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: PersonnelScorringServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 人事评分业务层接口的实现类
 * @date 2019/8/1210:55
 */
@Service
@Transactional
public class PersonnelScorringServiceImpl implements IPersonnelScorringService {
    @Resource
    PersonnelScorringMapper personnelScorringMapper;

    @Override
    public int deleteByPrimaryKey(Integer usercode) {
        return personnelScorringMapper.deleteByPrimaryKey(usercode);
    }

    @Override
    public int insertSelective(PersonnelScorring personnelScorring) {
        return personnelScorringMapper.insertSelective(personnelScorring);
    }

    @Override
    public PersonnelScorring selectByPrimaryKey(String usercode) {
        return personnelScorringMapper.selectByPrimaryKey(usercode);
    }

    @Override
    public int updateByPrimaryKeySelective(PersonnelScorring personnelScorring) {
        return personnelScorringMapper.updateByPrimaryKeySelective(personnelScorring);
    }

    @Override
    public List<PersonnelScorring> selectAllLike(String year, String month, String departmentname, String score1, String score2) {
        return personnelScorringMapper.selectAllLike(year, month, departmentname, score1, score2);
    }

    @Override
    public List<PersonnelScorring> selectAllLikeByManager(PersonnelScorring personnel) {
        return personnelScorringMapper.selectAllLikeByManager(personnel);
    }

    @Override
    public List<PersonnelScorring> selectListByMonthAndYear(PersonnelScorring personnelScorring) {
        return personnelScorringMapper.selectListByMonthAndYear(personnelScorring);
    }

    @Override
    public List<PersonnelScorring> selectListByMonthAndYearAndDepartmentName(String year, String month, String departmentname) {
        return personnelScorringMapper.selectListByMonthAndYearAndDepartmentName(year, month, departmentname);
    }

    @Override
    public int batchInsert(List<PersonnelScorring> list) {
        return personnelScorringMapper.batchInsert(list);
    }


    @Override
    public int batchDelete(List<Integer> usercodes) {
        return personnelScorringMapper.batchDelete(usercodes);
    }

    @Override
    public PersonnelScorring selectOne(PersonnelScorring scorring) {
        return personnelScorringMapper.selectOne(scorring);
    }

    @Override
    public void deleteOne(PersonnelScorring scorring) {
        personnelScorringMapper.deleteOne(scorring);
    }


}
